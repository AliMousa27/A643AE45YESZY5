# How to name language variables

Some rules to follow when naming variables:
 - Start with what page it concerns.
 - Be as specific as possible (e.g. `loginButton` instead of `button`).
 - Use camelCase where applicable.

---
### Example:

* `<page>_<element>`
* `initial_loginButton`

## Exceptions

If a value is the same across all pages, you skip adding it and just name it inside the page.

### Example:

- Company names that are the same for all languages (Google, Apple, Microsoft, ...)

# Where to put language variables

Keep all variables next to each other on a page by page basis.

---

### Example:
```
    "login-variable1": "text",
    "login-variable2": "text",
    "home-variable3": "text",
    "home-variable4": "text",
    "home-variable5": "text",
    "user-variable6": "text",
    ...
```

# What if a variable is used on multiple pages?

Make two variables with the same name, but with a different page prefix, as it promotes modifiability.

This way, if you want to change the variable on one page, you don't have to worry about it affecting the other page(s).

---

### Example:
```
    "login-example": "word1",
    "home-example": "word1",
    "home-test": "another word",
    "user-test": "another word",
    "user-example": "word1",
    ...
```