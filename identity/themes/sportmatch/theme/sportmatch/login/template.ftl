<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${msg("loginTitle",(realm.displayName!''))}</title>

    <link rel="stylesheet" href="${url.resourcesPath}/css/style.css">
</head>
<body>
<div class="container">
    <div class="card">
        <h1 class="logo">SportMatch</h1>

        <#if message?has_content>
            <div class="alert ${message.type}">
                ${message.summary}
            </div>
        </#if>

        <#nested>
    </div>
</div>
</body>
</html>