<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">

    <%--<link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.45/css/bootstrap-datetimepicker.css"--%>
    <%--rel="stylesheet">--%>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <%--<script src="//cdnjs.cloudflare.com/ajax/libs/moment.js/2.9.0/moment-with-locales.js"></script>--%>
    <%--<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.45/js/bootstrap-datetimepicker.min.js"></script>--%>
    <script src="../../../js/bootstrap-datetimepicker.min.js"></script>


    <link href="../../../css/stylesheet.css" rel="stylesheet" type="text/css">
    <link href="../../../css/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css">
    <title>SMM Jocker</title>

    <script>
        $(function () {
            $("#includedMenu").load("menu");
        });
    </script>
</head>
<body>
<div class="container">
    <div class="row">
        <div id="includedMenu"></div>
        <div class="col-sm-9 col-md-9">
            <div class="well">
                <h1>Tasker</h1>

                <form id="tasker" action="/invite/tasker/add" method="POST" class="form-horizontal">
                    <div class="form-group"><label class="col-xs-3 control-label">Assistant for Page</label>
                        <div class="col-xs-5 selectContainer">
                            <select class="form-control" name="assisId">
                                <c:forEach items="${assistants}" var="assistant">
                                    <option value="${assistant.getId()}">${assistant.getLogin()}
                                        : ${assistant.getPage().getPage()}  </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <%--<div class="form-group"><label class="col-xs-3 control-label">Start from:</label>--%>
                    <%--<div class="col-xs-5 selectContainer">--%>
                    <%--<input id="time" type="datetime" value="" class="form-control" name="time" placeholder="">--%>
                    <%--</div>--%>
                    <%--</div>--%>
                    <div class="form-group"><label class="col-xs-3 control-label">Start from:</label>
                        <div class="col-xs-5 selectContainer">
                            <div class="input-append date form_datetime">
                                <input id="datetime" name="datetime" type="text">
                                <span class="add-on"><i class="icon-th"></i></span>
                            </div>
                        </div>
                    </div>

                    <div class="form-group"><label class="col-xs-3 control-label">Per: </label>
                        <div class="col-xs-5 selectContainer">
                            <input type='number' class="form-control bfh-number" value="1" name="perCount"
                                   id="perCount"/>
                        </div>
                    </div>
                    <div class="form-group"><label class="col-xs-3 control-label">Hour/Day: </label>
                        <div class="col-xs-5 selectContainer">
                            <select class="form-control" name="perId">
                                <option value="1">Hour</option>
                                <option value="2">Day</option>
                            </select>
                        </div>
                    </div>
                    <button class="btn btn-default" type="submit">Add</button>
                </form>
                <label for="task_table">Your pages: </label>
                <table class="table table-striped" id="task_table">
                    <thead>
                    <tr>
                        <th width="20%"><b>Assistant</b></th>
                        <th width="20%"><b>Page</b></th>
                        <th width="20%"><b>Start from</b></th>
                        <th width="10%"><b>Per</b></th>
                        <th width="10%"><b>Hour/Day</b></th>
                        <th width="20%"><b>Action</b></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${tasks}" var="task">
                        <tr>
                            <td width="20%">${task.getLogin()}</td>
                            <td width="20%">${task.getPage().getPage()}</td>
                            <td width="20%"><fmt:formatDate value="${task.getTask().getStart()}"
                                                            pattern="dd.MM.yyyy - HH:mm"/></td>
                            <td width="10%">${task.getTask().getPerCount()}</td>
                            <td width="10%">
                                <c:choose>
                                    <c:when test="${task.getTask().getPerId()==1}">Hour</c:when>
                                    <c:otherwise>Day</c:otherwise>
                                </c:choose>
                            </td>
                            <td width="20%">
                                <button type="button" id="${task.getTask().getId()}" value="${task.getTask().getId()}"
                                        class="btn btn-default navbar-btn">
                                    <c:choose>
                                        <c:when test="${task.getTask().isGo()}">Stop</c:when>
                                        <c:otherwise>Start</c:otherwise>
                                    </c:choose>
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>


            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    var date = new Date();
    document.getElementById('datetime').value = +(date.getDate() < 10 ? '0' : '') + date.getDate() + '.' + (date.getMonth() < 10 ? '0' : '') + (date.getMonth() + 1) + '.' + date.getFullYear() + " - "
            + (date.getHours() < 10 ? '0' : '') + date.getHours() + ":" + (date.getMinutes() < 10 ? '0' : '') + date.getMinutes();


    $(".form_datetime").datetimepicker({
        format: "dd.mm.yyyy - hh:ii",
        todayBtn: true,
        weekStart: 1,
        autoclose: true,
        todayHighlight: true,
        startDate: new Date(),
    });

    $('.navbar-btn').click(function () {
        console.log($(this).attr('id'));
        var data = {'toSend[]': [], 'taskId': $(this).attr('id')};
        $.post("/invite/tasker/action", data, function (data, status) {
            window.location.reload();
        });
    });
</script>


</body>
</html>
