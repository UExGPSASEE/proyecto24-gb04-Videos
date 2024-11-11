<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tarjetas de Videos</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
	<c:forEach var="video" items="${videos}">
		<div class="container">
		        <!-- Tarjeta de Video 1 -->
		        <div class="card">
		            <img src="video.jpg" alt="Miniatura del video 1" class="thumbnail">
		            <div class="card-content">
		                <h2 class="title">{video.title}}</h2>
		                <p class="description">{video.description}</p>
		                <p class="date">{video.date}</p>
		            </div>
		        </div>
	  </c:forEach>
</body>
</html>