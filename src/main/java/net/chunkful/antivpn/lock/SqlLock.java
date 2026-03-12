package net.chunkful.antivpn.lock;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SqlLock implements Lock {

  private static final Logger log = LoggerFactory.getLogger(SqlLock.class);

  private final String url;
  private final String username;
  private final String password;

  private boolean connected = false;
  private ConnectionSource connectionSource;
  private Dao<StoredLock, Integer> dao;

  @JsonCreator
  public SqlLock(
      @JsonProperty("url") final String url,
      @JsonProperty("username") final String username,
      @JsonProperty("password") final String password
  ) {
    this.url = url;
    this.username = username;
    this.password = password;
  }

  @Override
  public LockResult check(final UUID uuid, final InetAddress address) {
    try {
      connect();
      final List<StoredLock> locks = dao.queryForEq("uuid", uuid);
      if (locks.isEmpty()) {
        return LockResult.SKIPPED;
      }
      return locks.stream().anyMatch(lock -> address.equals(lock.getAddress())) ? LockResult.MATCH
          : LockResult.NO_MATCH;
    } catch (final SQLException e) {
      log.error("Error while checking lock", e);
      return LockResult.SKIPPED;
    }
  }

  @Override
  public void addLock(final UUID uuid, final InetAddress address) {
    try {
      connect();
      dao.create(new StoredLock(uuid, address));
    } catch (final SQLException e) {
      log.error("Error while setting lock", e);
    }
  }

  private void connect() throws SQLException {
    if (!connected) {
      this.connectionSource = new JdbcPooledConnectionSource(url, username, password);
      this.dao = DaoManager.createDao(connectionSource, StoredLock.class);
      TableUtils.createTableIfNotExists(connectionSource, StoredLock.class);
      connected = true;
    }
  }

  @DatabaseTable(tableName = "antivpn_locks")
  private static class StoredLock {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, dataType = DataType.UUID)
    private UUID uuid;

    @DatabaseField(canBeNull = false, dataType = DataType.BYTE_ARRAY)
    private byte[] address;

    public StoredLock() {
    }

    public StoredLock(final UUID uuid, final InetAddress address) {
      this.uuid = uuid;
      this.address = address.getAddress();
    }

    public UUID getUuid() {
      return uuid;
    }

    public InetAddress getAddress() {
      try {
        return InetAddress.getByAddress(address);
      } catch (final UnknownHostException e) {
        throw new RuntimeException(e);
      }
    }
  }

}
