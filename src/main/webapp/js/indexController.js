var app = angular.module("myapp", []);
app.controller("myctrl", function ($scope, $http, $rootScope) {
    $http.get('/testapi1').then(function (response) {
        $scope.emps = response.data;

    }, reason => {
        alert("Lỗi")
    });

});