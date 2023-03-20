

app.controller("myctrl", function ($scope, $http, $rootScope,$location) {
    // console.log($location.path())
    // $scope.showLeftNav = $location.path() === '/videoDetail';
    $rootScope.videos = [];
    $http.get('/videos').then(function (response) {
        $rootScope.videos = response.data;
    }, reason => {
        // alert("Lỗi")
    });
    var iHam= document.querySelector("#iconCollapseMenu")
    var menuCollapse=document.querySelector("#leftNav")
    console.log(iHam)
    console.log(menuCollapse)
});

