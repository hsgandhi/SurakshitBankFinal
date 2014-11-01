<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
<html>
<body>
<!-- <form name="indexForm" action="login" method="post" target="TheWindow"> -->
<form name="indexForm" action="login" method="post">
Please allow the popup to view this website.
</form>
</body>
</html>

<script type="text/javascript">
//window.open('', "TheWindow", "toolbar=no, scrollbars=no, resizable=yes, top=0, left=0,channelmode=yes, fullscreen=yes, location=no,menubar=no,status=no,toolbar=no");
//window.open('', "TheWindow");
document.indexForm.submit();
</script>
<tags:commonJs />
