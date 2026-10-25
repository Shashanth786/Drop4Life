(function () {
    "use strict";

    var currentPath = window.location.pathname;
    var htmlIndex = currentPath.indexOf("/HTML/");
    var contextPath = "";

    if (htmlIndex !== -1) {
        contextPath = currentPath.substring(0, htmlIndex);
    }

    var loginUrl = contextPath + "/login.html";

    function redirectToLogin() {
        window.location.replace(loginUrl);
    }

    function verifySession() {
        document.documentElement.style.visibility = "hidden";

        fetch(contextPath + "/api/profile", {
            method: "GET",
            credentials: "same-origin",
            cache: "no-store",
            headers: {
                "Accept": "application/json"
            }
        })
        .then(function (response) {
            var contentType =
                response.headers.get("content-type") || "";

            // Reject expired sessions and redirects to login.
            if (
                response.status === 401 ||
                response.status === 403 ||
                response.redirected ||
                response.url.indexOf(loginUrl) !== -1 ||
                !response.ok ||
                contentType.indexOf("application/json") === -1
            ) {
                redirectToLogin();
                return;
            }

            // Only reveal the page after receiving a JSON response.
            document.documentElement.style.visibility = "visible";
        })
        .catch(function (error) {
            console.error("Session verification failed:", error);
            redirectToLogin();
        });
    }

    window.addEventListener("pageshow", function () {
        verifySession();
    });

}());