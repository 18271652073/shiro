$(document).ready(function () {
    $("#login").on('click', function () {
        $("#form").removeAttr('action');
        $("#form").attr('action', '/login/check');
        $("#form").submit();
    });
    $("#register").on('click', function () {
        $("#form").removeAttr('action');
        $("#form").attr('action', '/login/register');
        $("#form").submit();
    });
});