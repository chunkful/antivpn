package net.chunkful.antivpn.check;

import net.chunkful.antivpn.check.cache.sql.SqlCache;
import java.sql.SQLException;

public class ProxycheckCheckTest {

  public static void main(final String[] args) throws SQLException {
    final CacheAdapter check = new CacheAdapter(
        new SqlCache("jdbc:h2:mem:test", "sa", ""),
        new ProxycheckCheck(null, "")
    );
    final CheckResult result = check.check(AddressUtil.getOwnAddress());
    System.out.println("result = " + result);
  }

}
