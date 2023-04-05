/**
 * 
 */
var app = angular.module("myAdmin", ["ngRoute"]);
app.config(function($routeProvider){
    $routeProvider
    .when("/",{
        templateUrl : "/views/video.jsp",
        controller: 'HomeController'
    })
    .when("/video",{
        templateUrl : "/views/video.jsp",
        controller: 'videoController'
    })
    .when("/user",{
        templateUrl : "/views/user.jsp"
    })
    .when("/report",{
        templateUrl : "/views/index.jsp"
    })
    .otherwise({
        redirectTo: '/'
    });

});
app.controller("videoController", function ($scope, $http, $rootScope, $location, $routeParams, $route, $timeout, $window, $anchorScroll) {
    $scope.video;
    $scope.create = function(){
		$scope.video = {
			title: $scope.title,
			description : $scope.description,
			duration : $scope.duration,
			views : 0,
			thumbnailUrl : $scope.thumbnailUrl,
			videoUrl : $scope.videoUrl,
			user : $scope.userID
		};
		$http.post("api/admin/video/insert",$scope.video).then(function (response) {
			console.log(response.data);
           		if(response.data==null){
					    
				}else{
					
				}

        	});
	}
});