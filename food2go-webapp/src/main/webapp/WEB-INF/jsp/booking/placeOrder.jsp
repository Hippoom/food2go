<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Place Order</title>
</head>
<body>
	<h1>${error}</h1>
	<form:form
		action="${pageContext.request.contextPath}/booking/placeOrder"
		modelAttribute="command">
		<table>
			<tr>
				<th>Street1</th>
				<th>Street2</th>
				<th>Delivery time</th>
			</tr>
			<tr>
				<td><form:input path="deliveryAddressStreet1" />
					<form:errors path="deliveryAddressStreet1" /></td>
				<td><form:input path="deliveryAddressStreet2" />
					<form:errors path="deliveryAddressStreet2" /></td>
				<td><form:input path="deliveryTime" />
					<form:errors path="deliveryTime" /></td>
			</tr>
		</table>
		<input type="submit" value="Submit">
	</form:form>
</body>
</html>