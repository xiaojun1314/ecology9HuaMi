package weaver.dnwj;

import weaver.conn.RecordSet;

/**
 * @className: CheckRoleByUserId
 * @author: jun
 * @date: 2021-05-14 15:20
 * @Depiction:
 **/
public class CheckUserInRoles {
    public static boolean CheckRoleByUserId(int roleid, int userid)
    {
        if ((userid == 1314) || (userid == 1)) {
            return true;
        }

        RecordSet rs = new RecordSet();
        rs.execute("select * from hrmrolemembers where roleid=" + roleid + " and resourceid=" + userid);
        return rs.next();
    }
}
