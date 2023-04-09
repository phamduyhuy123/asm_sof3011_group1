var app = angular.module("myapp", ['ngRoute']);

app.controller("myctrl", function ($scope, $http, $rootScope, $location, $routeParams, $route, $timeout, $window, $anchorScroll) {
    $anchorScroll();
    $rootScope.videos = [];
    $rootScope.isCollapsed = false;
    $rootScope.user;
    var user = localStorage.getItem('user');
    if (user) {
        $rootScope.user = JSON.parse(user);
    }
    $rootScope.relativeTimeFilter = function (dateUpload) {
        return moment(dateUpload).fromNow();
    }
    $rootScope.logOut = function () {
        $rootScope.user = null;
        localStorage.setItem('user', null);
        $location.path("/login");
    }
    $rootScope.login = function (username, password) {
        $http.post("user/login?username=" + username + "&password=" + password).then(function (response) {
            if (response.data.error) {
                $scope.errorMsg = response.data.error;
            } else {
                $rootScope.user = response.data;
                $scope.errorMsg = null;
                console.log($rootScope.user)
                localStorage.setItem('user', JSON.stringify($rootScope.user));
                $location.path("/home");
            }

        });
    }
    // $http.get('api/findUser?userId=' + 2).then(function (response) {
    //    $rootScope.user = response.data;
    //     console.log($rootScope.user)
    // });
    $rootScope.getSearchResults = function () {
        $timeout(function () {
            $http.get('/search', {
                params: {q: $rootScope.searchQuery}
            }).then(function (response) {
                $rootScope.searchResults = response.data;
            });
        }, 1000);
    };
    $rootScope.next = function (number) {
        $location.path("/forgotpassword" + number);
    }
    $rootScope.$on('$routeChangeSuccess', function (event, current, previous) {
        $timeout(function () {
            var element = document.querySelector('.main-content');
            if (element) {
                element.scrollTop = 0;
            }
        });
        if ($location.path().includes('videoDetail')) {
            $rootScope.isCollapsed = true;
        } else {
            $rootScope.isCollapsed = false;
        }
    });
    $rootScope.checkLogin=function(){
        if(!$rootScope.user){
            alert("Please login to use this fearture")
            return false;
        }else{
            return true;
        }
    }
});
app.controller("homectrl", function ($scope, $http, $rootScope, $location, $routeParams) {
    $scope.videos = [];
    $scope.page = 1;
    $scope.pageSize = 8;
    $scope.limit = 8
    $scope.loadMore = function () {
        console.log("loadmore called" + $scope.page)
        var offset = ($scope.page - 1) * $scope.pageSize;
        $http.get('videos?offset=' + offset + '&limit=' + $scope.pageSize).then(function (response) {
            var newItems = response.data;
            $scope.videos = $scope.videos.concat(newItems);
            $rootScope.videos = $scope.videos;
            $scope.limit = $scope.videos.length;
            $scope.page += 1;
        });
    };
    $scope.loadMore();

});
app.controller("editprofilectrl", function ($scope, $http, $rootScope, $routeParams, $location, $q) {
    if(!$rootScope.user){
        alert("Please login to use this feature")
        $location.path("/login")
    }

});
app.controller("videoDetailCtrl", function ($scope, $http, $rootScope, $routeParams, $location, $q) {
    $scope.video;
    var searchObject = $location.search();
    $scope.videoUrl = "";
    $scope.videoLikes = [];
    $scope.comments = [];
    var videoId = searchObject.videoId;
    $http.get('videoDetail?videoId=' + videoId).then(function (response) {
        $scope.video = response.data;
        console.log($scope.video)
        $scope.loadVideo($scope.video.id);


    });

    $scope.likeCount = function (isLike) {
        if (isLike) {
            return $scope.videoLikes.likes;
        } else {
            return $scope.videoLikes.dislikes;
        }
    }
    $scope.isUserLikedVideo = function (isLike) {
        console.log(isLike)
        if (isLike) {
            $scope.checkbox1 = true;
        } else {
            $scope.checkbox2 = true;
        }
    }
    $scope.likeVideo = function (checkboxNumber, userId) {
        if (checkboxNumber === 1 && $scope.checkbox2) {
            $scope.checkbox2 = false;
        } else if (checkboxNumber === 2 && $scope.checkbox1) {
            $scope.checkbox1 = false;
        }

    };

    $scope.commentsData = {};
    $scope.loadChildrenComments = function (parentId) {
        if ($scope.commentsData[parentId]) {
            return $scope.commentsData[parentId];
        } else {
            var promise = $http.get('api/comment/get/childrenComment?parentId=' + parentId).then(function (response) {
                $scope.commentsData[parentId] = response.data;
                return response.data;
            });
            $scope.commentsData[parentId] = promise;
            // console.log(promise)
            return promise;
        }
    };
    $scope.player = null;
    $scope.playCount = 0;
    $scope.initPlayer = function () {
        $scope.player = new Plyr("#plyr-video", {
            speed: {selected: 1, options: [0.5, 0.75, 1, 1.25, 1.5, 1.75, 2]}
        });
        $scope.player.on('play', (event) => {
            $scope.playCount++;
            console.log($scope.playCount)
            if ($scope.playCount == 1) {

            }
        });
    };
    $scope.loadVideo = function (id) {
        if (!$rootScope.user) {
            $http.get('streamVideo?videoId=' + id).then(function (response) {
                $scope.videoUrl = response.data;
                $scope.initPlayer();
            });
        } else {
            $http.get('streamVideo?videoId=' + id + "&userId=" + $rootScope.user.id).then(function (response) {
                $scope.videoUrl = response.data;
                $scope.initPlayer();
            });
        }
        $http.get('api/like/get/likeCount?videoId=' + videoId).then(function (response) {
            if (response.data) {
                $scope.videoLikes = response.data;
            }
        });
        if ($rootScope.user) {
            $http.get('api/like/get/likeVideo?videoId=' + videoId + '&userId=' + $rootScope.user.id).then(function (response) {
                if (response.data) {
                    $scope.isUserLikedVideo(response.data.like)
                }
            });
        }
        $http.get('api/comments?videoId=' + videoId).then(function (response) {
            $scope.comments = response.data;
            console.log($scope.comments)

        });
    };
    $scope.sendCommentToVideo = function (value, videoId, userId) {
        $http.post('api/comment/post/commentVideo?videoId=' + videoId + '&userId=' + userId, value).then(function (response) {
            $scope.comments.unshift(response.data);

        });

    };
    $scope.sendChildrenCommentToVideo = function (value, videoId, userId, commentId) {
        console.log(commentId)
        $http.post('api/comment/post/commentVideo?videoId=' + videoId + '&userId=' + userId + '&parentId=' + commentId, value).then(function (response) {
            console.log($scope.commentsData[commentId])
            $scope.commentsData[commentId].push(response.data);
            console.log($scope.commentsData[commentId])

        });
    };

});
app.controller("loginctrl", function ($scope, $http, $rootScope, $location, $routeParams, $route, $timeout, $window, $anchorScroll) {


});
app.controller("registerctrl", function ($scope, $http, $rootScope, $location, $routeParams, $route, $timeout, $window, $anchorScroll) {
    $scope.userdangky;
    $scope.dangky = function () {
        $scope.userdangky = {
            username: $scope.username,
            email: $scope.email,
            password: $scope.password,
            role: "USER"
        }
        $http.post("user/dangky", $scope.userdangky).then(function (response) {
            console.log(response.data)
            if (response.data.error) {
                $scope.errorMsg = response.data.error.split('\n');
            } else if (response.data == null) {

            } else {
                $scope.errorMsg = null;
                $rootScope.user = response.data;
                $location.path("/home");
            }

        });

    }
});
app.controller("userChanelctrl", function ($scope, $http, $rootScope, $location, $routeParams, $route, $q) {
    $scope.videos = [];
    if ($rootScope.user) {
        $http.get('video/userVideo?userId=' + $rootScope.user.id).then(function (response) {

            if (response.data) {
                console.log(response.data)
                $scope.videos = response.data;
            } else {
                alert("Failed to get videos")
            }
        })
    }else{
        alert("Please login to use this fearture");
        $location.path("/login")
    }
    $scope.isLogin = function () {
        if (!$rootScope.user) {
            if (confirm("Please login to use this feature")) {
                $('#uploadVideo').modal('hide');
                setTimeout(
                    $location.path("/login")
                    , 1000);
            }
        }
    }
    $scope.uploadVideo = function () {
        if ($rootScope.user) {
            var formData = new FormData();
            formData.append('videoTitle', $scope.title);
            formData.append('videoDescription', $scope.description);
            formData.append('videoFile', document.getElementById('videoFile').files[0]);
            formData.append('videoThumbnailFile', document.getElementById('videoThumbnailFile').files[0]);
            formData.append('user', JSON.stringify($rootScope.user));
            $http.post('video/upload', formData,
                {
                    transformRequest: angular.identity,
                    headers: {
                        'Content-Type': undefined
                    }
                }
            ).then(function (response) {
                if (response.data.error) {
                    alert(response.data.error)
                } else {
                    $('#uploadVideo').modal('hide');
                    console.log(response.data)
                    $scope.videos.push(response.data);
                }
            });
        } else {

        }
    }
    $scope.selectedVideos = {};
    $scope.hasSelected = false;
    $scope.updateSelected = function () {
        $scope.hasSelected = false;
        for (var id in $scope.selectedVideos) {
            if ($scope.selectedVideos[id]) {
                $scope.hasSelected = true;
                break;
            }
        }
    };
    $scope.videos = [];
    $scope.doAction = function () {
        var selectedVideos = [];
        for (var id in $scope.selectedVideos) {
            if ($scope.selectedVideos[id]) {
                selectedVideos.push(id);
            }
        }
        console.log(selectedVideos)
        var deletePromises = selectedVideos.map(function (id) {
            console.log(id)
            return $http.delete('api/admin/video/delete?videoId=' + id);
        });
        $q.all(deletePromises).then(function (responses) {

            for (var i = $scope.videos.length - 1; i >= 0; i--) {
                if (selectedVideos.indexOf($scope.videos[i].id.toString()) !== -1) {
                    $scope.videos.splice(i, 1);
                }
            }
        });

    };
});
app.controller("viewhistoryctrl", function ($scope, $http, $rootScope, $location, $routeParams, $route, $timeout, $window, $filter) {
    $scope.viewsHistory = [];
    if ($rootScope.user) {
        $http.get("api/user/get/viewHistory?userId=" + $rootScope.user.id).then(function (response) {
            if (response.data) {
                $scope.viewsHistory = response.data;
                console.log(response.data);
                $scope.viewsHistory.forEach(element => {
                    element.viewDate = $filter('date')(element.viewDate, 'dd/MM/yyyy HH:mm:ss');
                });
            }
        })
    }else{
        alert("Please login to use this fearture");
        $location.path("/login")
    }


});
app.controller("forgotctrl", function ($scope, $http, $rootScope, $location, $routeParams, $route, $timeout, $window, $anchorScroll) {


});
app.controller("forgot2ctrl", function ($scope, $http, $rootScope, $location, $routeParams, $route, $timeout, $window, $anchorScroll) {


});
app.controller("forgot3ctrl", function ($scope, $http, $rootScope, $location, $routeParams, $route, $timeout, $window, $anchorScroll) {


});

