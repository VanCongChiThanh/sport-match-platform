<#import "template.ftl" as layout>

<@layout.layout>
    <form action="${url.registrationAction}" method="post">

        <div class="form-group">
            <label for="username">Username</label>
            <input id="username" name="username" type="text"
                   value="${(register.formData.username!'')}"
                   required />
        </div>

        <div class="form-group">
            <label for="email">Email</label>
            <input id="email" name="email" type="email"
                   value="${(register.formData.email!'')}"
                   required />
        </div>

        <div class="form-group">
            <label for="firstName">First name</label>
            <input id="firstName" name="firstName" type="text"
                   value="${(register.formData.firstName!'')}" />
        </div>

        <div class="form-group">
            <label for="lastName">Last name</label>
            <input id="lastName" name="lastName" type="text"
                   value="${(register.formData.lastName!'')}" />
        </div>

        <div class="form-group">
            <label for="password">Password</label>
            <input id="password" name="password" type="password" required />
        </div>

        <div class="form-group">
            <label for="password-confirm">Confirm Password</label>
            <input id="password-confirm" name="password-confirm"
                   type="password" required />
        </div>

        <div class="form-group">
            <button type="submit" class="btn">Register</button>
        </div>

        <div class="footer">
            <a href="${url.loginUrl}">Back to login</a>
        </div>

    </form>
</@layout.layout>