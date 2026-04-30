// 获取登录用户
function getUser() {
    const userStr = localStorage.getItem("user");
    if (!userStr) {
        alert("请先登录");
        window.location.href = "login.html";
        return null;
    }
    return JSON.parse(userStr);
}

// 保存用户
function setUser(user) {
    localStorage.setItem("user", JSON.stringify(user));
}

// 退出登录
function logout() {
    localStorage.removeItem("user");
    window.location.href = "login.html";
}

// GET请求
async function get(url) {
    const res = await fetch(BASE_URL + url);
    return res.json();
}

// POST请求
async function post(url, data) {
    const res = await fetch(BASE_URL + url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    });
    return res.json();
}

// PUT请求
async function put(url, data) {
    const res = await fetch(BASE_URL + url, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    });
    return res.json();
}

// DELETE请求
async function del(url) {
    const res = await fetch(BASE_URL + url, {
        method: "DELETE"
    });
    return res.json();
}

//返回主页面
function goHome() {
    const user = getUser();

    if (!user) {
        window.location.href = "login.html";
        return;
    }

    if (user.role === "ADMIN") {
        window.location.href = "admin-home.html";
    } else if (user.role === "TEACHER") {
        window.location.href = "teacher-home.html";
    } else if (user.role === "STUDENT") {
        window.location.href = "student-home.html";
    } else {
        window.location.href = "login.html";
    }
}