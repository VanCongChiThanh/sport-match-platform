<#import "template.ftl" as layout>

<@layout.layout>
    <form action="${url.loginAction}" method="post">

        <div class="form-group">
            <label for="username">Username</label>
            <input id="username" name="username" type="text"
                   value="${(login.username!'')}"
                   required autofocus />
        </div>

        <div class="form-group">
            <label for="password">Password</label>
            <input id="password" name="password" type="password" required />
        </div>

        <div class="form-group">
            <button type="submit" class="btn">Login</button>
        </div>

        <#if realm.registrationAllowed>
            <div class="footer">
                <a href="${url.registrationUrl}">Create account</a>
            </div>
        </#if>

    </form>
</@layout.layout>