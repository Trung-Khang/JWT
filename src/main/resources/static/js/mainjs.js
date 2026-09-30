/* global $ */
(function () {
  'use strict';
  var tokenKey = 'jwt-demo-token';
  function message(text) { $('#message').text(text || ''); }
  function logout() { localStorage.removeItem(tokenKey); window.location.assign('/login'); }
  function unauthorized(xhr) { if (xhr && xhr.status === 401) { message('Phiên đăng nhập không hợp lệ hoặc đã hết hạn.'); setTimeout(logout, 800); return true; } return false; }
  $(function () {
    var login = $('#login-form');
    if (login.length) {
      login.on('submit', function (event) {
        event.preventDefault();
        var button = $('#login-button'); if (button.prop('disabled')) return;
        button.prop('disabled', true); message('');
        $.ajax({ url: '/auth/login', method: 'POST', contentType: 'application/json', data: JSON.stringify({ email: $('#email').val(), password: $('#password').val() }) })
          .done(function (data) { localStorage.setItem(tokenKey, data.token); window.location.assign('/user/profile'); })
          .fail(function (xhr) { message(xhr.responseJSON && xhr.responseJSON.error ? xhr.responseJSON.error : 'Không thể đăng nhập.'); })
          .always(function () { button.prop('disabled', false); });
      }); return;
    }
    if ($('#profile-image').length) {
      var token = localStorage.getItem(tokenKey); if (!token) { logout(); return; }
      $('#logout-button').on('click', logout);
      $.ajax({ url: '/users/me', headers: { Authorization: 'Bearer ' + token } })
        .done(function (user) { $('#full-name').text(user.fullName || 'Chưa cập nhật'); $('#profile-email').text(user.email || ''); if (user.images) $('#profile-image').attr('src', user.images).attr('alt', 'Ảnh đại diện'); })
        .fail(function (xhr) { if (!unauthorized(xhr)) message('Không tải được hồ sơ.'); });
    }
  });
}());
