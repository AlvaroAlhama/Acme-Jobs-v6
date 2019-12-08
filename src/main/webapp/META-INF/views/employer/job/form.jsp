<%@page language = "java"%>

<%@taglib prefix = "jstl" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix = "acme" tagdir = "/WEB-INF/tags"%>

<acme:form>
	
	<jstl:if test="${command == 'create'}">
		<acme:form-textbox code="employer.job.form.reference" path="reference" placeholder="EEEE-JJJJ"/>
	</jstl:if>
	<jstl:if test="${command != 'create'}">
		<acme:form-textbox code="employer.job.form.reference" path="reference" readonly="true"/>
	</jstl:if>
	
	<acme:form-textbox code="employer.job.form.title" path="title"/>
	<acme:form-moment code="employer.job.form.deadline" path="deadline"/>
	<acme:form-money code="employer.job.form.salary" path="salary" placeholder="30000 EUR" /> 
	<acme:form-url code="employer.job.form.moreInfo" path="moreInfo"/>
	
	<jstl:if test="${command != 'create'}">
		<acme:form-select code="authenticated.job.form.status" path="status">
			<acme:form-option code="authenticated.job.form.status.draft" value="DRAFT"/>
			<acme:form-option code="authenticated.job.form.status.published" value="PUBLISHED"/>
		</acme:form-select>
	</jstl:if>
	
	<acme:form-submit test="${command == 'create'}" 
	    code="employer.job.form.button.create" action="/employer/job/create"/>
	<acme:form-submit test="${command == 'show'}" 
		code="employer.job.form.button.update" action="/employer/job/update"/>
	<acme:form-submit test="${command == 'update'}" 
		code="employer.job.form.button.update" action="/employer/job/update"/>	
	<acme:form-submit test="${command == 'show'}" 
		code="employer.job.form.button.delete" action="/employer/job/delete"/>
		
	
	<acme:form-return code="employer.job.form.return"/>	
	
</acme:form>

<jstl:if test="${command == 'show'}">

	<jstl:if test="${!descriptorExist}">
		<acme:redirect-button code="employer.job.redirect.create-descriptor" action="/employer/descriptor/create?jobId=${id}"/>
	</jstl:if>
	
	<jstl:if test="${descriptorExist}">
		<acme:redirect-button code="employer.job.redirect.descriptor" action="/employer/descriptor/show?id=${id}"/>
	</jstl:if>
	
	<acme:redirect-button code="employer.job.redirect.auditRecord" action="/employer/audit-records/list?id=${id}"/>
	
</jstl:if>