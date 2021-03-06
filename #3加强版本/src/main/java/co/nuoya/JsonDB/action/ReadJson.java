package co.nuoya.JsonDB.action;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import co.nuoya.JsonDB.model.Customer;
import co.nuoya.JsonDB.service.CustomerService;
import co.nuoya.JsonDB.service.CustomerServiceImpl;
import co.nuoya.JsonDB.util.Utils;

public class ReadJson extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String fileName = req.getParameter("filename");
		session.setAttribute("fileName", fileName);
		Utils.getFileLogger().info("文件名:{}",fileName);
		String url = req.getServletContext().getRealPath("/WEB-INF/classes/resources/" + fileName);
		File file = new File(url);
		if (file.exists()) {
			Utils.getFileLogger().info("readJson getRealPath:{}",req.getServletContext().getRealPath("/WEB-INF/classes/resources/" + fileName));//OK
			Utils.getFileLogger().info("readJson getResource:{}",req.getServletContext().getResource("/WEB-INF/classes/resources/" + fileName).getPath());//OK
			Utils.getFileLogger().info("readJson ReadJson.class.getClassLoader().getResource:{}",ReadJson.class.getClassLoader().getResource("/resources/" + fileName).getPath());//OK
			
			List<String> readResult = readJson(url);
			session.setAttribute("resultMsg", "执行成功！");
			session.setAttribute("result", true);
			session.setAttribute("readResult", readResult.toString());
			resp.sendRedirect("./resultForRead.jsp");
			
		} else {
			session.setAttribute("result", false);
			session.setAttribute("resultMsg", "文件不存在！");
		}
	}
	
	public List<String> readJson(String path){
		Utils.getFileLogger().info("readJson start");
		Utils.getFileLogger().info("paramter path : {}",path);
		//也可以使用以下格式
		//Utils.getFileLogger().info("paramter path : {}",path);
		//Utils.getFileLogger().printf(Level.INFO, "paramter path : {}",path);
		/**
		 * DB操作结果
		 */
		List<String> result = new LinkedList<String>();
		/**
		 * 从json文件中读取数据，生成list
		 */
		List<Customer> customer = Utils.readJsonToList(path,Customer.class);
		
		CustomerService custService = new CustomerServiceImpl();
		customer.forEach($->{
			switch ($.getOperate()) {
			case "add":
				Utils.getFileLogger().debug("insert customer data : {}", $.toString());
				//result.add($.getId());
				result.add(addCutomer(custService, $));
				Utils.getFileLogger().info("the result of insert customer data : {}",result.get(result.size()-1));
			break;
			case "del":
				Utils.getFileLogger().debug("delete customer data : {}", $.toString());
				//result.add($.getId());
				result.add(deleteCustomer(custService, $));
				Utils.getFileLogger().info("the result of delete customer data : {}",result.get(result.size()-1));
				break;
			case "upd":
				Utils.getFileLogger().debug("update customer data : {}", $.toString());
				//result.add($.getId());
				result.add(updateCustomer(custService, $));
				Utils.getFileLogger().info("the result of update customer data : {}",result.get(result.size()-1));
				break;
			}
		});
		Utils.getFileLogger().info("readJson end");
		return result;
	}

	public String updateCustomer(CustomerService custService, Customer customer) {
		Utils.getFileLogger().debug("updateCustomer start");
		Utils.getFileLogger().debug("paramter custService : {} ; customer : {}", custService.toString(), customer.toString());
		if (isCustomerExist(customer.getId())) {
			if (custService.updateCustomer(customer) == 1) {
				Utils.getFileLogger().debug("updateCustomer result : {}","更新成功");
				Utils.getFileLogger().debug("updateCustomer end");
				return "更新成功";
			}
		} else {
			Utils.getFileLogger().debug("updateCustomer result : {}","顾客不存在");
			Utils.getFileLogger().debug("updateCustomer end");
			return "顾客不存在";
		}
		Utils.getFileLogger().debug("updateCustomer result : {}","更新失败");
		Utils.getFileLogger().debug("updateCustomer end");
		return "更新失败";
	}

	public String deleteCustomer(CustomerService custService, Customer customer) {
		Utils.getFileLogger().debug("deleteCustomer start");
		Utils.getFileLogger().debug("paramter custService : {} ; customer : {}", custService.toString(), customer.toString());
		if (isCustomerExist(customer.getId())) {
			if (custService.deleteCustomer(customer.getId()) == 1) {
				Utils.getFileLogger().debug("deleteCustomer result : {}","删除成功");
				Utils.getFileLogger().debug("deleteCustomer end");
				return "删除成功";
			}
		} else {
			Utils.getFileLogger().debug("deleteCustomer result : {}","顾客不存在");
			Utils.getFileLogger().debug("deleteCustomer end");
			return "顾客不存在";
		}
		Utils.getFileLogger().debug("deleteCustomer result : {}","删除失败");
		Utils.getFileLogger().debug("deleteCustomer end");
		return "删除失败";
	}

	public String addCutomer(CustomerService custService, Customer customer) {
		Utils.getFileLogger().debug("addCutomer start");
		Utils.getFileLogger().debug("paramter custService : {} ; customer : {}", custService.toString(), customer.toString());
		if (!isCustomerExist(customer.getId())) {
			if (custService.addCustomer(customer) == 1) {
				Utils.getFileLogger().debug("addCutomer result : {}","添加成功");
				Utils.getFileLogger().debug("addCutomer end");
				return "添加成功";
			}
		} else {
			Utils.getFileLogger().debug("addCutomer result : {}","顾客已存在");
			Utils.getFileLogger().debug("addCutomer end");
			return "顾客已存在";
		}
		Utils.getFileLogger().debug("addCutomer result : {}","添加失败");
		Utils.getFileLogger().debug("addCutomer end");
		return "添加失败";
	}

	/**
	 * 查看顾客是否存在
	 * @param id 顾客ID
	 * @return true：存在    false：不存在
	 */
	private boolean isCustomerExist(String id) {
		Utils.getFileLogger().debug("isCustomerExist start");
		Utils.getFileLogger().debug("paramter id : {} ", id);
		CustomerService custService = new CustomerServiceImpl();
		List<Customer> result = custService.findCustomer(id);
		Utils.getFileLogger().debug("result : {}",result.size() > 0 ? true : false);
		Utils.getFileLogger().debug("isCustomerExist end");
		return result.size() > 0 ? true : false;
	}
	/**
	 * 删除顾客信息
	 */
	public int deleteDB() {
		Utils.getFileLogger().info("deleteDB start");
		CustomerService custService = new CustomerServiceImpl();
		int result = custService.deleteCustomer(null);
		Utils.getFileLogger().info("result : {} ", result);
		Utils.getFileLogger().info("deleteDB end");
		return result;
	}
	/**
	 * 查询并输出所有顾客信息
	 */
	public List<Customer> findAllCustomer() {
		Utils.getFileLogger().info("findAllCustomer start");
		CustomerService custService = new CustomerServiceImpl();
		List<Customer> result = custService.findCustomer(null);
		//Utils.getFileLogger().info("result : {} ", result.toString());
		Utils.getFileLogger().info("findAllCustomer end");
		return result;
	}
}
