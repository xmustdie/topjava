package ru.javawebinar.topjava.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

public class BadHtmlValidator implements ConstraintValidator<SanitizeHtml, String> {

    // http://owasp-java-html-sanitizer.googlecode.com/svn/trunk/distrib/javadoc/org/owasp/html/HtmlPolicyBuilder.html
    // builder is not thread safe, so make local
    private static final PolicyFactory DISALLOW_ALL = new HtmlPolicyBuilder().toFactory();

    @Override
    public void initialize(SanitizeHtml constraintAnnotation)
    {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context)
    {
        String sanitized = DISALLOW_ALL.sanitize(value);
        return sanitized.equals(value);
    }
}
