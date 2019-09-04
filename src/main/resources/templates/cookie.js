// maxAge: Seconds
function writeCookies(maxAge) {
    let expires = new Date();
    expires.setSeconds(expires.getSeconds() + maxAge);

    let selectedColor = document.getElementById("favoriteColor").value;
    let cookie1 = "favoriteColor=" + selectedColor + ";";
    cookie1 += "Expires=" + expires.toUTCString() + ";";
    cookie1 += "Path=/"; // Path: Required in Chrome!!

    // Write Cookie 1
    document.cookie = cookie1;


    let email = document.getElementById("email").value;
    let cookie2 = "fullName=" + email + ";";
    cookie2 += "Max-Age=" + maxAge + ";"; // Seconds
    cookie2 += "Path=/"; // Path: Required in Chrome!!

    // Write Cookie 2
     document.cookie = cookie2;
    alert("Write Successful!");
}

function killCookies() {
    writeCookies(0);
}

function readCookie() {
    var allCookies = document.cookie;

    let logText = "All Cookies : " + allCookies + "<br/>";

    // Get all the cookies pairs in an array
    let cookieArray = allCookies.split(';');

    // Now take key value pair out of this array
    for (var i = 0; i < cookieArray.length; i++) {
        let kvArray = cookieArray[i].split('=');
        if(kvArray[0] == "email") {
        	$('input[value=email]').val(kvArray[1]);
        }
    }

    document.getElementById("logArea").innerHTML = logText;
}