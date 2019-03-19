<%@tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@attribute name="name" required="true" type="java.lang.String" %>
<%@attribute name="value" required="true" type="gov.idaho.isp.forensic.time.domain.persistence.CriteriaDate" %>

<span class="float-right">
  <div class="btn-group btn-group-toggle" data-toggle="buttons">
    <label class="btn btn-outline-primary btn-xs ${value.searchType == 'ON' ? 'active' : ''}" onclick="$('[data-${name}-input-2]').addClass('d-none').val('');">
      <input type="radio" name="${name}.searchType" value="ON" ${value.searchType == 'ON' ? 'checked' : ''}/> On
    </label>
    <label class="btn btn-outline-primary btn-xs ${value.searchType == 'BEFORE' ? 'active' : ''}" onclick="$('[data-${name}-input-2]').addClass('d-none').val('');">
      <input type="radio" name="${name}.searchType" value="BEFORE" ${value.searchType == 'BEFORE' ? 'checked' : ''}/> Before
    </label>
    <label class="btn btn-outline-primary btn-xs ${value.searchType == 'AFTER' ? 'active' : ''}" onclick="$('[data-${name}-input-2]').addClass('d-none').val('');">
      <input type="radio" name="${name}.searchType" value="AFTER" ${value.searchType == 'AFTER' ? 'checked' : ''}/> After
    </label>
    <label class="btn btn-outline-primary btn-xs ${value.searchType == 'BETWEEN' ? 'active' : ''}" style="margin-right: 0;" onclick="$('[data-${name}-input-2]').removeClass('d-none');">
      <input type="radio" name="${name}.searchType" value="BETWEEN" ${value.searchType == 'BETWEEN' ? 'checked' : ''}/> Between
    </label>
  </div>
</span>
<div class="input-group" style="width: 100%;">
  <input type="date" class="form-control" name="${name}.date1" value="${value.date1}" data-${name}-input-1 placeholder="yyyy-mm-dd">
  <input type="date" class="form-control ${value.searchType == 'BETWEEN' ? '' : 'd-none'}" name="${name}.date2" value="${value.date2}" data-${name}-input-2 placeholder="yyyy-mm-dd">
</div>