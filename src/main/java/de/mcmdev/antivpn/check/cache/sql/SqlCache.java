package de.mcmdev.antivpn.check.cache.sql;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;
import de.mcmdev.antivpn.check.cache.Cache;
import de.mcmdev.antivpn.check.CheckResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public final class SqlCache implements Cache {

  private static final Logger log = LoggerFactory.getLogger(SqlCache.class);

  private final String url;
  private final String username;
  private final String password;

  private boolean connected = false;
  private ConnectionSource connectionSource;
  private Dao<StoredResult, Integer> dao;

  @JsonCreator
  public SqlCache(
      @JsonProperty("url") final String url,
      @JsonProperty("username") final String username,
      @JsonProperty("password") final String password
  ) {
    this.url = url;
    this.username = username;
    this.password = password;
  }

  @Override
  public void put(@NotNull final InetAddress address, @NotNull final CheckResult result) {
    try {
      connect();
      if (dao.queryForEq("address", address.getAddress()).isEmpty()) {
        dao.create(new StoredResult(address, result));
      } else {
        dao.update(new StoredResult(address, result));
      }
    } catch (final SQLException e) {
      log.error("Error while storing result in database", e);
    }
  }

  @Override
  public @Nullable CheckResult get(@NotNull final InetAddress address) {
    try {
      connect();
      final List<StoredResult> results = dao.queryForEq("address", address.getAddress());
      if (results.isEmpty()) {
        return null;
      }
      return results.getFirst().getResult();
    } catch (final SQLException e) {
      log.error("Error while retrieving result from database", e);
      return null;
    }
  }

  private void connect() throws SQLException {
    if(!connected) {
      this.connectionSource = new JdbcPooledConnectionSource(url, username, password);
      this.dao = DaoManager.createDao(connectionSource, StoredResult.class);
      TableUtils.createTableIfNotExists(connectionSource, StoredResult.class);
      connected = true;
    }
  }

  @DatabaseTable(tableName = "antivpn_results")
  private static class StoredResult {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, dataType = DataType.BYTE_ARRAY)
    private byte[] address;

    @DatabaseField(canBeNull = false)
    private CheckResult result;

    @DatabaseField(canBeNull = false)
    private Timestamp createdAt;

    public StoredResult() {
    }

    public StoredResult(final InetAddress address, final CheckResult result) {
      this.address = address.getAddress();
      this.result = result;
      this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    public InetAddress getAddress() {
      try {
        return InetAddress.getByAddress(address);
      } catch (final UnknownHostException e) {
        throw new RuntimeException(e);
      }
    }

    public CheckResult getResult() {
      return result;
    }

  }

}
