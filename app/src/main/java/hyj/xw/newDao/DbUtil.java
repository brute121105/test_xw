package hyj.xw.newDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Administrator on 2018/1/10.
 */

public class DbUtil {

   public static SQLiteDatabase mDatabase = DBHelper.getDatabase();


    // 表名
    // null。数据库如果插入的数据为null，会引起数据库不稳定。为了防止崩溃，需要传入第二个参数要求的对象
    // 如果插入的数据不为null，没有必要传入第二个参数避免崩溃，所以为null
    // 插入的数据
    public static void insertData(String text) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.NAME, text);
        values.put(DBHelper.AGE, 17);
        long a = mDatabase.insert(DBHelper.TABLE_NAME, null, values);
        System.out.println("aa-->"+a);
    }

    // 表名
    // 删除条件
    // 满足删除的值
    public static void deleteData() {
        int count = mDatabase.delete(DBHelper.TABLE_NAME, DBHelper.NAME + " = ?", new String[]{"鹿晗"});
    }

    // 表名
    // 修改后的数据
    // 修改条件
    // 满足修改的值
    public static void updateData() {
        ContentValues values = new ContentValues();
        values.put(DBHelper.NAME, "小茗同学");
        values.put(DBHelper.AGE, 18);
        int count = mDatabase
                .update(DBHelper.TABLE_NAME, values, DBHelper.NAME + " = ?", new String[]{"鹿晗"});
    }

    // 表名
    // 查询字段
    // 查询条件
    // 满足查询的值
    // 分组
    // 分组筛选关键字
    // 排序
    public static void queryData() {
        Cursor cursor = mDatabase.query(DBHelper.TABLE_NAME,
                new String[]{DBHelper.NAME, DBHelper.AGE},
                DBHelper.AGE + " > ?",
                new String[]{"16"},
                null,
                null,
                DBHelper.AGE + " desc");// 注意空格！

        int nameIndex = cursor.getColumnIndex(DBHelper.NAME);
        int ageIndex = cursor.getColumnIndex(DBHelper.AGE);
        while (cursor.moveToNext()) {
            String name = cursor.getString(nameIndex);
            String age = cursor.getString(ageIndex);
            Log.d("1507", "name-->: " + name + ", age: " + age);
        }

    }
}
