(function () {
    var KEY = 'reviewer-theme';

    function currentTheme() {
        return document.documentElement.getAttribute('data-theme') || 'dark';
    }

    function applyTheme(theme) {
        document.documentElement.setAttribute('data-theme', theme);
        localStorage.setItem(KEY, theme);
        var chk = document.getElementById('themeToggle');
        if (chk) chk.checked = (theme === 'light');
    }

    function injectSwitch() {
        var wrap = document.createElement('div');
        wrap.className = 'theme-switch-wrap';
        wrap.innerHTML =
            '<span class="theme-label">Dark</span>' +
            '<label class="theme-switch" title="Cambia tema">' +
                '<input type="checkbox" id="themeToggle">' +
                '<span class="theme-slider"></span>' +
            '</label>' +
            '<span class="theme-label">Chiaro</span>';
        document.body.prepend(wrap);

        var chk = document.getElementById('themeToggle');
        chk.checked = (currentTheme() === 'light');
        chk.addEventListener('change', function () {
            applyTheme(this.checked ? 'light' : 'dark');
        });
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', injectSwitch);
    } else {
        injectSwitch();
    }
})();
