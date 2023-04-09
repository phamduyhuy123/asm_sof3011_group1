<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!-- <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet"
integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"
integrity="sha384-w76AqPfDkMBDXo30jS1Sgez6pr3x5MlQ1ZAGC+nuZB+EYdgRZgiwxhTBTkF7CXvN"
crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
<script src="https://kit.fontawesome.com/2d8a439e72.js" crossorigin="anonymous"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.2/angular.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.2/angular-route.min.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.3/font/bootstrap-icons.css"> -->
<div class="container mt-5">
    <ul class="nav nav-tabs" id="myTab" role="tablist">
        <li class="nav-item" role="presentation">
            <button class="nav-link active text-bg-dark" id="home-tab" data-bs-toggle="tab" data-bs-target="#home"
                    type="button" role="tab" aria-controls="home" aria-selected="true"><i
                    class="bi bi-pencil-square"></i> Cập nhật
            </button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link text-bg-dark" id="profile-tab" data-bs-toggle="tab" data-bs-target="#profile"
                    type="button" role="tab" aria-controls="profile" aria-selected="false"><i class="bi bi-list"></i>
                Danh sách
            </button>
        </li>
    </ul>
    <div class="tab-content" id="myTabContent">
        <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
            <div class="card card-default">

                <div class="card-body bg-dark text-light">
                    <div class="row">
                        <div class="col-sm-4 text-center  align-middle align-items-center justify-content-center d-flex">
                            <img id="image-preview"  src="video/poster?videoId={{video.id}}"  height="200px" alt="">
                        </div>
                        <div class="col-sm-8">
                            <form enctype="multipart/form-data">
                                <div class="mb-3">
                                    <label for="youtube-id" class="form-label">Video ID</label>
                                    <input ng-model="video.id" type="text" class="form-control text-bg-dark"
                                           id="youtube-id">
                                </div>
                                <div class="mb-3">
                                    <label for="youtube-id" class="form-label">Video Title</label>
                                    <input ng-model="video.title" name="videoTitle" type="text"
                                           class="form-control text-bg-dark" id="youtube-id">
                                </div>
                                <div class="mb-3">
                                    <label for="youtube-id" class="form-label">Duration</label>
                                    <input ng-model="video.duration " type="text" class="form-control text-bg-dark"
                                           id="youtube-id">
                                </div>
                                <div class="mb-3">
                                    <label for="youtube-id" class="form-label">Date upload</label>
                                    <input ng-model="video.uploadDate" name="uploadDate" type="text"
                                           class="form-control text-bg-dark" id="">
                                </div>
                                <div class="mb-3">
                                    <label for="youtube-id" class="form-label">Video URL</label>
                                    <input accept="video/* " ng-model="video.videoUrl" name="videoFile" type="file"
                                           class="form-control text-bg-dark" id="videoFile">
                                </div>
                                <div class="mb-3">
                                    <label for="youtube-id" class="form-label">Thumbnail URL</label>
                                    <input accept="image/*" ng-model="thumbnailUrl" name="videoThumbnailFile" type="file"
                                           class="form-control text-bg-dark" id="videoThumbnailFile"
                                            ng-change="loadImage()">
                                </div>
                                <div class="mb-3">
                                    <label for="youtube-id" class="form-label">Views</label>
                                    <input ng-model="video.views" name="views" type="number"
                                           class="form-control text-bg-dark" id="youtube-id">
                                </div>
                                <div class="mb-3">
                                    <label for="youtube-id" class="form-label">User ID</label>
                                    <input ng-model="video.user.id" name="user" type="text"
                                           class="form-control text-bg-dark" id="youtube-id">
                                </div>


                            </form>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="form-floating">
                                <textarea ng-model="video.description" name="videoDescription"
                                          class="form-control text-bg-dark" placeholder="Leave a Description here"
                                          id="floatingTextarea2" style="height: 100px"></textarea>
                                <label for="floatingTextarea2">Description</label>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12 mt-3">

                            <button ng-click="create()" class="btn btn-secondary float-end">
                                Create
                            </button>


                            <button ng-click="update(video.id,true)" class="btn btn-secondary float-end me-3">
                                Update
                            </button>


                            <button ng-click="delete(video.id)" class="btn btn-secondary float-end me-3">
                                Delete
                            </button>


                            <button ng-click="reset()" class="btn btn-secondary float-end me-3">
                                Reset
                            </button>

                        </div>
                    </div>
                </div>

            </div>
        </div>

        <div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">
            <table class="table table-bordered table-dark text-light">
                <tr>
                    <th>Video id</th>
                    <th>Video title</th>
                    <th>Thumbnail img</th>
                    <th>Duration bigin</th>
                    <th>Date upload</th>
                    <th>Video URL</th>
                    <th>View</th>
                    <th>User ID</th>
                    <th>Description</th>
                    <th></th>

                </tr>
                <tr ng-repeat="v in videos">
                    <td>{{v.id}}</td>
                    <td>{{v.title}}</td>
                    <td><img width="50px" height="50px" src="video/poster?videoId={{v.id}}"></td>
                    <td>{{v.duration}}</td>
                    <td>{{v.uploadDate | date:'yyyy-MM-dd HH:mm:ss'}}</td>
                    <td>{{v.videoUrl}}</td>
                    <td>{{v.views}}</td>
                    <td>{{v.user.id}}</td>
                    <td>{{v.description}}</td>
                    <td>
                        <a ng-click="update(v.id,false)" type="button">
                            edit</a>
                    </td>
                </tr>

            </table>
        </div>
    </div>