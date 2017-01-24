<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

    <link href="../../../css/stylesheet.css" rel="stylesheet" type="text/css">
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
                <h1>Bind assistant to you facebook page</h1>

                <button type="button" id="buy_assis" class="btn btn-default navbar-btn">Buy assistant</button>
                <form class="form-inline" action="/invite/bind_assistant/bind" method="POST">
                    <div class="form-group"><label class="col-xs-3 control-label">Page</label>
                        <div class="col-xs-5 selectContainer">
                            <select class="form-control" name="pageId">
                                <c:forEach items="${userPages}" var="userPage">
                                    <option value="${userPage.getId()}">${userPage.getPage()}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group"><label class="col-xs-3 control-label">Assistant</label>
                        <div class="col-xs-5 selectContainer">
                            <select class="form-control" name="assisId">
                                <c:forEach items="${userAssistants}" var="userAssistant">
                                    <option value="${userAssistant.getId()}">${userAssistant.getLogin()}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <button class="btn btn-default" type="submit">Bind</button>
                </form>

                <label for="page_table">Your pages and assistant: </label>
                <table class="table table-striped" id="page_table">
                    <thead>
                    <tr>
                        <th width="40%"><b>Page</b></th>
                        <th width="60%"><b>Assistant</b></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${pages}" var="page">
                        <c:forEach items="${page.getAssistants()}" var="assistant">
                            <tr>
                                <td width="40%">${page.getPage()}</td>
                                <td width="60%">${assistant.getLogin()}</td>
                            </tr>
                        </c:forEach>
                    </c:forEach>
                    </tbody>
                </table>


            </div>
        </div>
    </div>
</div>

<script>
    $('.navbar-btn').click(function () {

        var data = {'toSend[]': [], 'assisId': $(this).attr('id')};
        $.post("/invite/bind_assistant/buy", data, function (data, status) {
            window.location.reload();
        });
    });
</script>

</body>
</html>
