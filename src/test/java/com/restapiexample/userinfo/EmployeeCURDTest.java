package com.restapiexample.userinfo;

import com.restapiexample.constants.EndPoints;
import com.restapiexample.model.EmployeePojo;
import com.restapiexample.testbase.TestBase;
import com.restapiexample.utils.TestUtils;
import io.restassured.http.ContentType;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static org.hamcrest.Matchers.hasValue;


@RunWith(SerenityRunner.class)
public class EmployeeCURDTest extends TestBase {
    static String employee_name = "Bella" + TestUtils.getRandomValue();
    static String employee_salary = "30000";
    static String employee_age="28";
    static int employeeId;

    @Title("This will create a new employee")
    @Test
    public void test001() {
        EmployeePojo employeePojo =new EmployeePojo();
        employeePojo.setName(employee_name);
        employeePojo.setSalary(employee_salary);
        employeePojo.setAge(employee_age);

        SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .body(employeePojo)
                .when()
                .post(EndPoints.CREATE_EMPLOYEE)
                .then().log().all().statusCode(201);
    }

    @Title("Verify if the employee was added to the application")
    @Test
    public void test002() {
        employee_name="Tiger Nixon";
        String p1 = "data.findAll{it.employee_name=='";
        String p2 = "'}.get(0)";
        HashMap<String, Object> employeeMap = SerenityRest.given().log().all()
                .when()
                .get(EndPoints.GET_ALL_EMPLOYEES)
                .then()
                .statusCode(200)
                .extract()
                .path(p1 + employee_name + p2);
        Assert.assertThat(employeeMap, hasValue(employee_name));
        employeeId = (int) employeeMap.get("id");
        System.out.println(employeeId);
    }
    @Title("Update the employee information and verify the updated information for ID=5")
    @Test
    public void test003(){
        employee_name = "Riya34";
        employee_salary="80000";
        employee_age="28";
        employeeId=1;
        EmployeePojo employeePojo = new EmployeePojo();
        employeePojo.setName(employee_name);
        employeePojo.setSalary(employee_salary);
        employeePojo.setAge(employee_age);
        SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("employeeID", employeeId)
                .body(employeePojo)
                .when()
                .put(EndPoints.UPDATE_EMPLOYEE_BY_ID)
                .then().log().all().statusCode(200);

    }
    @Title("Delete the employee and verify if the employee is deleted! for ID=2")
    @Test
    public void test004(){
        employeeId=1;
        SerenityRest.given().log().all()
                .pathParam("employeeID", employeeId)
                .when()
                .delete(EndPoints.DELETE_EMPLOYEE_BY_ID)
                .then().statusCode(200)
                .log().status();

        SerenityRest.given().log().all()
                .pathParam("employeeID", employeeId)
                .when()
                .get(EndPoints.GET_SINGLE_EMPLOYEE_BY_ID)
                .then()
                .statusCode(404);
    }

}
