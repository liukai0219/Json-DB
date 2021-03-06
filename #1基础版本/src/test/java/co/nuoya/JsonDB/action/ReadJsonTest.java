package co.nuoya.JsonDB.action;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import co.nuoya.JsonDB.DBUtil.DBUtils;
import co.nuoya.JsonDB.model.Customer;
import co.nuoya.JsonDB.util.Utils;;

public class ReadJsonTest {
	
	@Before
	public void setUp() throws SQLException {
		Connection conn = DBUtils.getConnection();
		Statement stat = conn.createStatement();
		String sql = "DELETE FROM Customer";
		stat.executeUpdate(sql);
		sql = "INSERT INTO customer(id, _index, guid, active, balance, picture, age, eyecolor, lastName, firstName, company, email, phone, address, about, favoriteFruit, created, createdBy, updated, updatedBy) VALUES ('999999999999999','0','05f531f2-ab5a-470f-b77d-9be077909412',0,'$1,489.98','http://placehold.it/32x32',31,'blue','Chavez','Christi','ZEROLOGY','christi.chavez@zerology.ca','+1(928)536-3512','889 Stryker Court, Glenbrook, Michigan, 2079','Ex proident adipisicing sunt nostrud deserunt reprehenderit ut do. Ex enim laboris esse ullamco aliquip ullamco exercitation adipisicing tempor exercitation. Aliqua ex proident nostrud excepteur duis culpa. Anim ea fugiat tempor labore. Sunt nulla id aliqua quis id culpa. Sunt mollit elit excepteur enim pariatur aute. Aliquip eiusmod commodo id aliquip exercitation ut.','apple',sysdate(),'root',sysdate(),'root');";
		stat.executeUpdate(sql);
	}
	
	
	@Test
	public void readJson(){
		ReadJson readJson = new ReadJson();
		List<String> result = readJson.readJson("./src/main/resources/customers.json");
		
		assertEquals(14,result.size());
		//成功添加
		assertEquals("添加成功",result.get(0));
		assertEquals("添加成功",result.get(1));
		assertEquals("添加成功",result.get(2));
		assertEquals("添加成功",result.get(3));
		assertEquals("添加成功",result.get(4));
		assertEquals("添加成功",result.get(5));
		assertEquals("添加成功",result.get(6));
		//更新成功
		assertEquals("更新成功",result.get(7));
		assertEquals("更新成功",result.get(8));
		//删除成功
		assertEquals("删除成功",result.get(9));
		assertEquals("删除成功",result.get(10));
		//操作不成功
		assertEquals("顾客不存在",result.get(11));
		assertEquals("顾客不存在",result.get(12));
		assertEquals("顾客已存在",result.get(13));
		
	}
	
	@Test
	public void findAllCustomer() {
		ReadJson readJson = new ReadJson();
		List<Customer> result = readJson.findAllCustomer();
		
		assertEquals(1,result.size());
	}
	
	@Test
	public void deleteDB() {
		ReadJson readJson = new ReadJson();
		assertEquals(1,readJson.deleteDB());
	}
}
