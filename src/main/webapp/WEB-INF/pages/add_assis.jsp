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
                <c:choose>
                    <c:when test="${assistant ne null}">
                        <h1>Edit Assistant</h1>
                        <form class="form-inline" action="/invite/add_assistant/update" method="POST">
                            <label class="sr-only" for="login">Login</label>
                            <input type="text" value="${assistant.getLogin()}" class="form-control mb-2 mr-sm-2 mb-sm-0"
                                   id="login" name="login"
                                   placeholder="Login">

                            <label class="sr-only" for="pass">Password</label>
                            <input type="text" value="${assistant.getPassword()}" class="form-control" id="pass"
                                   name="pass"
                                   placeholder="Password">
                            <input type="hidden" value="${assistant.getId()}" class="form-control" id="assisId"
                                   name="assisId">
                            <button class="btn btn-default" type="submit">Save</button>

                        </form>
                    </c:when>
                    <c:otherwise>
                        <h1>Add Assistant</h1>
                        <form class="form-inline" action="/invite/add_assistant/add" method="POST">
                            <label class="sr-only" for="login">Login</label>
                            <input type="text" class="form-control mb-2 mr-sm-2 mb-sm-0" id="login" name="login"
                                   placeholder="Login">

                            <label class="sr-only" for="pass">Password</label>
                            <input type="text" class="form-control" id="pass" name="pass" placeholder="Password">
                            <button class="btn btn-default" type="submit">Add</button>

                        </form>
                    </c:otherwise>
                </c:choose>


                <label for="assistants_table">Assistants: </label>
                <table class="table table-striped" id="assistants_table">
                    <thead>
                    <tr>
                        <th width="40%"><b>Login</b></th>
                        <th width="40%"><b>Password</b></th>
                        <th width="20%"><b>Action</b></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${assistants}" var="assis">
                        <tr>
                            <td width="40%">${assis.getLogin()}</td>
                            <td width="40%">${assis.getPassword()}</td>
                            <td width="20%">
                                <button type="button" id="${assis.getId()}" value="${assis.getId()}"
                                        class="btn btn-default navbar-btn">Edit
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

<script>

    $('.navbar-btn').click(function () {

        var data = {'toSend[]': [], 'assisId': $(this).attr('id')};
        $.post("/invite/add_assistant/edit", data, function (data, status) {
            window.location.reload();
        });
    });
</script>

</body>
</html>
