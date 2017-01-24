<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

    <link href="../../../css/stylesheet.css" rel="stylesheet" type="text/css">
    <title>Add your Facebook Pages</title>

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
                <h1>Add your Facebook Pages</h1>
                <form class="form-inline" action="/invite/pages/add" method="POST">
                    <label for="page-url">Add Page: </label>
                    <div class="input-group">
                        <span class="input-group-addon" id="basic-addon3">https://www.facebook.com/</span>
                        <input type="text" class="form-control" id="page-url" name="page-url"
                               aria-describedby="basic-addon3">
                    <span class="input-group-btn">
                        <button class="btn btn-default" type="submit">Add</button>
                    </span>
                    </div>
                </form>

                <label for="page_table">Your pages: </label>
                <table class="table table-striped" id="page_table">
                    <thead>
                    <tr>
                        <th width="70%"><b>Page</b></th>
                        <th width="30%"><b>Action</b></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${pages}" var="page">
                        <tr>
                            <td width="70%">${page.getPage()}</td>
                            <td width="30%">
                                <button type="button" id="${page.getId()}" value="${page.getId()}"
                                        class="btn btn-default navbar-btn">Delete
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

        var data = {'toSend[]': [], 'pageId': $(this).attr('id')};
        $.post("/invite/pages/del", data, function (data, status) {
            window.location.reload();
        });
    });
</script>

</body>
</html>
