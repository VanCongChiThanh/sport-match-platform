<#import "template.ftl" as layout>

<@layout.layout>
    <div style="text-align: center;">
        <div style="font-size: 24px; font-weight: bold; margin-bottom: 15px; color: #e74c3c;">
            <#if message?has_content>
                ${message.type!'Error'}
            <#else>
                Error
            </#if>
        </div>
        
        <div style="font-size: 16px; margin-bottom: 25px;">
            <#if message?has_content>
                ${message.summary}
            <#else>
                Something went wrong. Please try again.
            </#if>
        </div>
        
        <a href="${url.loginAction}" class="btn">Back to Login</a>
    </div>
</@layout.layout>