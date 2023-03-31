<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.3.0/font/bootstrap-icons.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js" integrity="sha384-w76AqPfDkMBDXo30jS1Sgez6pr3x5MlQ1ZAGC+nuZB+EYdgRZgiwxhTBTkF7CXvN" crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.2/angular.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.2/angular-route.min.js"></script>
    <style> 
    .offcanvas-header .btn-close {
       display: none;
    }  
    .form-control::placeholder{
      color: white;
    }  
    .card {
   border-radius: 0;
}
</style>
  </head>
<body class="bg-dark" ng-app="myappAdmin">
    <div class="row">
        <div class="col-sm-4">
          <nav class="navbar navbar-dark ">
            <div class="container-fluid">
              <button class="navbar-toggler me-4" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasDarkNavbar" aria-controls="offcanvasDarkNavbar">
                <span class="navbar-toggler-icon"></span>
              </button><a class="nav-link navbar-brand me-auto" id="offcanvasDarkNavbarLabel" href="#">Youtube Admin <i class="bi bi-youtube text-danger"></i></a>
              <div class="offcanvas offcanvas-start text-bg-dark" tabindex="-1" id="offcanvasDarkNavbar" aria-labelledby="offcanvasDarkNavbarLabel">
                <div class="offcanvas-header">
                  <a class="nav-link navbar-brand" id="offcanvasDarkNavbarLabel" href="#">Youtube Admin <i class="bi bi-youtube text-danger"></i></a>
                  <button type="button" class="btn-close btn-close-white" data-bs-dismiss="offcanvas" aria-label="Close"></button>
                </div>
                <div class="offcanvas-body">
                  <ul class="navbar-nav justify-content-end flex-grow-1 pe-3 mt-3">
                    <li class="nav-item">
                      <a class="nav-link active" aria-current="page" href="#!home">Home</a>
                    </li>
                    <li class="nav-item mt-4">
                      <a class="nav-link" href="#!video">Videos</a>
                    </li>
                    <li class="nav-item mt-4">
                      <a class="nav-link" href="#!user">Users</a>
                    </li>
                    <li class="nav-item mt-4">
                      <a class="nav-link" href="#!report">Reports</a>
                    </li>
                    <li class="nav-item dropdown mt-4">
                      <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        Show more
                      </a>
                      <ul class="dropdown-menu dropdown-menu-dark">
                        <li><a class="dropdown-item" href="#">Information</a></li>
                        <li>
                          <hr class="dropdown-divider">
                        </li>
                        <li>
                        <li><a class="dropdown-item" href="#">Log out</a></li></li>
                      </ul>
                    </li>
                  </ul>
                </div>f
              </div>
            </div>
          </nav>
        </div>
        <div class="col-sm-4">
          <form class="d-flex" role="search">
            <input class="form-control me-2 mt-3 bg-dark text-light" type="search" placeholder="Search" aria-label="Search">
            <button class="btn btn-outline mt-3 text-light" type="submit"><i class="bi bi-search"></i></button>
          </form>
        </div>
        <div class="col-sm-4">
          <img src="img/bee.png" height="40" class="rounded-circle bg-light mt-3 float-end me-4" alt="">
        </div> 
    </div>
    <ng-view></ng-view>
<script src="../js/admin/index.js"></script>
<script src="../js/route.js"></script>
    <script src="js/admin/index.js"></script>
    <script src="js/route.js"></script>
</body>
</html>