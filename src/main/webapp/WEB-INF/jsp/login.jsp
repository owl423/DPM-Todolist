<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>로그인 - Check Check</title>
  <script src="https://unpkg.com/vue@2.5.3/dist/vue.min.js"></script>
  <link rel="stylesheet" href="/static/css/login.css" type="text/css">
</head>
<body>
  <div class="wrapper">
    <header class="header">
      <div class="center">
        <h1>
            <img class="logo" src="/static/images/Logo.svg" alt="checkcheck logo">
        </h1>
      </div>
    </header>
    <main id="main">
      <login></login>
    </main>
    <footer>
      <a href="/register" class="create-account" title="회원가입하기">Create Account</a>
    </footer>
  </div>
  <script type="text/x-template" id="login-template">
    <form @submit.prevent>
      <div class="rect">
        <label for="username">아이디</label>
        <input type="text" id="username" placeholder="Username" v-model="username">
      </div>
      <div class="rect">
        <label for="password">비밀번호</label>
        <input type="text" id="password" placeholder="Password" v-model="password">
      </div>
        <button class="login-btn rect" @click="login" @touch="login">Login</button>
      </form>
  </script>
  <script src="/static/scripts/login.js"></script>
</body>
</html>