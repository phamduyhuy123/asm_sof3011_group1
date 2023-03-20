app.controller("videoDetailCtrl", function ($scope, $http, $rootScope,$routeParams) {
    $rootScope.isVideoDetailShowing=true;
    $scope.video;
    $http.get('/video/'+$routeParams.id).then(function (response) {
        $scope.video=response.data;
    }, reason => {
        // alert("Lỗi")
    });
});