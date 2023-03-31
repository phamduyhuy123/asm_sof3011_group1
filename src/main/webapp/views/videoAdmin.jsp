<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="container mt-5">
    <ul class="nav nav-tabs" id="myTab" role="tablist">
      <li class="nav-item" role="presentation">
        <button class="nav-link active text-bg-dark" id="home-tab" data-bs-toggle="tab" data-bs-target="#home" type="button" role="tab" aria-controls="home" aria-selected="true"><i class="bi bi-pencil-square"></i> Cập nhật</button>
      </li>
      <li class="nav-item" role="presentation">
        <button class="nav-link text-bg-dark" id="profile-tab" data-bs-toggle="tab" data-bs-target="#profile" type="button" role="tab" aria-controls="profile" aria-selected="false"><i class="bi bi-list"></i> Danh sách</button>
      </li>
    </ul>
    <div class="tab-content" id="myTabContent">
      <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
          <div class="card card-default">
              
              <div class="card-body bg-dark text-light">
                  <div class="row">
                    <div class="col-sm-4 text-center mt-4">
                      <img src="img/bee.png" height="200px" alt="">
                    </div>
                    <div class="col-sm-8">
                      <form>
                        <div class="mb-3">
                          <label for="youtube-id" class="form-label">Video ID</label>
                          <input type="text" class="form-control text-bg-dark" id="youtube-id">
                        </div>
                        <div class="mb-3">
                          <label for="youtube-id" class="form-label">Video Title</label>
                          <input type="text" class="form-control text-bg-dark" id="youtube-id">
                        </div>
                        <div class="mb-3">
                          <label for="youtube-id" class="form-label">Duration bigin</label>
                          <input type="text" class="form-control text-bg-dark" id="youtube-id">
                        </div>
                        <div class="mb-3">
                          <label for="youtube-id" class="form-label">Date upload</label>
                          <input type="text" class="form-control text-bg-dark" id="youtube-id">
                        </div>
                        <div class="mb-3">
                          <label for="youtube-id" class="form-label">Video URL</label>
                          <input type="text" class="form-control text-bg-dark" id="youtube-id">
                        </div>
                        <div class="mb-3">
                          <label for="youtube-id" class="form-label">View bigin</label>
                          <input type="number" class="form-control text-bg-dark" id="youtube-id">
                        </div>
                        <div class="mb-3">
                          <label for="youtube-id" class="form-label">User ID</label>
                          <input type="text" class="form-control text-bg-dark" id="youtube-id">
                        </div>
                        

                      </form>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-sm-12">
                      <div class="form-floating">
                        <textarea class="form-control text-bg-dark" placeholder="Leave a Description here" id="floatingTextarea2" style="height: 100px"></textarea>
                        <label  for="floatingTextarea2">Description</label>
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-sm-12 mt-3">
                      <button class="btn btn-secondary float-end">
                        Create
                      </button>
                      <button class="btn btn-secondary float-end me-3">
                        Update
                      </button>
                      <button class="btn btn-secondary float-end me-3">
                        Delete
                      </button>
                      <button class="btn btn-secondary float-end me-3">
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
                <th>View bigin</th>
                <th>User ID</th>
                <th>Description</th>
              </tr>
              <tr>
                <td>1</td>
                <td>Cách đấm thằng ngồi bên khi đi học</td>
                <td>anhdamsmlthangngoiben.png</td>
                <td>1:00:00</td>
                <td>29/03/2023</td>
                <td>youtube.com/damthangbansml</td>
                <td>1M</td>
                <td>Daicabancuoi</td>
                <td>Video chỉ mang tính chất minh họa xin hãy thực hiện với thằng bạn thân ngồi bên của bạn nhé!!</td>
              </tr>
              
            </table>
        </div>
  </div>