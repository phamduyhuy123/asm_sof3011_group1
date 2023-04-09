/**
 *
 */
const dateTimePattern = /^(\d{2})\/(\d{2})\/(\d{4})\s(\d{2}):(\d{2}):(\d{2})$/;
var app = angular.module("myAdmin", ["ngRoute"]);
app.config(function ($routeProvider) {
    $routeProvider

        .when("/video", {
            templateUrl: "/views/video.jsp",
            controller: 'videoController'
        })
        .when("/user", {
            templateUrl: "/views/user.jsp",
            controller: 'videoController'
        })
        // .when("/report", {
        //     templateUrl: "/views/index.jsp"
        // })
        .otherwise({
            redirectTo: '/video'
        });

});
app.controller("videoController", function ($scope, $http, $rootScope, $location, $routeParams, $filter) {
    $scope.imagePreview = 'video/poster?videoId={{video.id}}';

    $scope.loadImage=function (){
        const fileInput = document.getElementById('videoThumbnailFile');
        const imagePreview = document.getElementById('image-preview');
       if(fileInput.files.length>0){
           var fildeReader= new FileReader();
           fildeReader.onload=function (event){
               imagePreview.setAttribute("src",event.target.result);
           }
           fildeReader.readAsDataURL(fileInput.files[0])
       }
   }

    $scope.videos = [];
    $http.get('api/admin/videos').then(function (response) {
        $scope.videos = response.data;
        $scope.videos.forEach(element => {
            element.uploadDate = $filter('date')(element.uploadDate, 'dd/MM/yyyy HH:mm:ss');
        });
        $scope.video=$scope.videos[0];
    })

    $scope.create = function () {

        var formData = new FormData();
        formData.append('videoTitle', $scope.video.title);
        formData.append('videoDescription', $scope.video.description);
        formData.append('videoFile', document.getElementById('videoFile').files[0]);
        formData.append('videoThumbnailFile', document.getElementById('videoThumbnailFile').files[0]);
        formData.append('uploadDate', new Date($scope.video.uploadDate).getTime());
        formData.append('user', $scope.video.user.id);
        console.log($scope.video.description);
        $http.post("api/admin/video/insert", formData,
            {
                transformRequest: angular.identity,
                headers: {
                    'Content-Type': undefined
                }
            }
        ).then(function (response) {
            console.log(response.data);
            if (response.data.error) {
                alert(response.data.error);
            } else {
                console.log(response.data)
                response.data.uploadDate = $filter('date')(response.data.uploadDate, 'dd/MM/yyyy HH:mm:ss');
                $scope.videos.push(response.data);
            }

        });

    }
    $scope.update = function (id, page) {
        if (page === false) {
            $scope.video = $scope.videos.find(item => item.id === id);
            console.log($scope.video)
            document.getElementById("home-tab").click()
            $scope.imagePreview='video/poster?videoId={{video.id}}';
        } else {
            var formData = new FormData();
            formData.append('videoTitle', $scope.video.title);
            formData.append('videoDescription', $scope.video.description);
            formData.append('videoFile', document.getElementById('videoFile').files[0]);
            formData.append('videoThumbnailFile', document.getElementById('videoThumbnailFile').files[0]);
            formData.append('views', $scope.video.views)
            formData.append('uploadDate', new Date($scope.video.uploadDate).getTime())
            formData.append('user', $scope.video.user.id);
            $http.put('api/admin/video/edit?videoId=' + $scope.video.id, formData,
                {
                    transformRequest: angular.identity,
                    headers: {
                        'Content-Type': undefined
                    }
                }
            ).then(function (response) {
                if (response.data.error){
                    alert(response.data.error)
                    return;
                }
                if (response.data) {
                    alert("Update video " + response.data + " success")
                }
                // if (response.data.error) {
                //     $scope.errorMsg = response.data.error;
                // } else {
                //     console.log(response.data)
                //     $scope.videos[$scope.videos.findIndex(el => el.id ===response.data)];
                // }

            });
        }
    }
    $scope.delete = function (id) {
        $http.delete('api/admin/video/delete?videoId=' + id).then(function (response) {
            if (response.data) {
                alert(response.data)
            }
        })
    }
    $scope.reset = function () {
        $scope.video = null;
    }
});