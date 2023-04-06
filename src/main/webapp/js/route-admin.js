/**
 * 
 */
var app = angular.module("myAdmin", ["ngRoute"]);
app.config(function($routeProvider){
    $routeProvider
    .when("/",{
        templateUrl : "/views/video.jsp",
        controller: 'videoController'
    })
    .when("/video",{
        templateUrl : "/views/video.jsp",
        controller: 'videoController'
    })
    .when("/user",{
        templateUrl : "/views/user.jsp",
        controller: 'videoController'
    })
    .when("/report",{
        templateUrl : "/views/index.jsp"
    })
    .otherwise({
        redirectTo: '/'
    });

});
app.controller("videoController", function ($scope, $http, $rootScope, $location, $routeParams, $route, $timeout, $window, $anchorScroll) {
    $scope.videos=[];
    $http.get('api/admin/videos').then(function(response){
		$scope.videos=response.data;
	})
    $scope.create = function(){
		var formData = new FormData();
            formData.append('videoTitle', $scope.video.title);
            formData.append('videoDescription', $scope.video.description);
            formData.append('videoFile', document.getElementById('videoFile').files[0]);
            formData.append('videoThumbnailFile', document.getElementById('videoThumbnailFile').files[0]);
            formData.append('user',$scope.video.user.id);
            console.log($scope.video.description);
		$http.post("api/admin/video/insert",formData,
                {
                    transformRequest: angular.identity,
                    headers: {
                        'Content-Type': undefined
                    }
                }
		
		 ).then(function (response) {
			console.log(response.data);
           		if (response.data.error) {
                    $scope.errorMsg=response.data.error;
                } else {
                    console.log(response.data)
                    $scope.videos.push(response.data);
                }

        	});
	}
	$scope.update = function(id){
		$scope.video= $scope.videos.find(item => item.id === id);
		console.log($scope.video)
		document.getElementById("home-tab").click();
	}
});