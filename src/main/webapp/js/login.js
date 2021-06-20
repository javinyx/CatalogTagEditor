// this function is executed when document is ready and makes variables not end in public scope
$(document).ready(function() {
    $("#loginbutton").on("click", function(e) {
        e.preventDefault();
        let form = document.getElementById("loginForm");
        if (form.checkValidity()) {
            let username = document.getElementById("username").value;
            let password = document.getElementById("password").value;

            $.ajax({
                type: "POST",
                url: 'CheckLogin',
                data: 'username=' + username + '&password=' + password,
                success: function(response) {
                    sessionStorage.setItem('username', username);
                    window.location.href = "index.html";
                    document.getElementById("errormessage").textContent = "Logged in";
                    document.getElementById("errormessage").style.display = "inline-block";
                },
                error: function(req) {
                    document.getElementById("errormessage").textContent = req.responseText;
                    document.getElementById("errormessage").style.display = "inline-block";
                },
            });
        } else {
            form.reportValidity();
        }
    })
})