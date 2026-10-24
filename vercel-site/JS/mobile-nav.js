/* Accessible mobile navigation for Drop4Life pages with multiple nav links. */
(() => {
  'use strict';

  const scopes = document.querySelectorAll('.drop-navbar, .navbar-inner, nav.navbar');

  scopes.forEach((scope, index) => {
    const menu = scope.querySelector(':scope > .drop-nav-links, :scope > .nav-links, :scope > .nav-actions');
    if (!menu || scope.querySelector(':scope > .mobile-nav-toggle')) return;

    scope.classList.add('mobile-nav-scope');

    const toggle = document.createElement('button');
    toggle.type = 'button';
    toggle.className = 'mobile-nav-toggle';
    toggle.setAttribute('aria-expanded', 'false');
    toggle.setAttribute('aria-controls', `drop4life-mobile-menu-${index + 1}`);
    toggle.setAttribute('aria-label', 'Open navigation menu');
    toggle.innerHTML = '<span class="mobile-nav-icon" aria-hidden="true"></span><span class="mobile-nav-label">Menu</span>';
    menu.id = menu.id || `drop4life-mobile-menu-${index + 1}`;
    scope.insertBefore(toggle, menu);

    const closeMenu = () => {
      scope.classList.remove('mobile-menu-open');
      toggle.setAttribute('aria-expanded', 'false');
      toggle.setAttribute('aria-label', 'Open navigation menu');
      const label = toggle.querySelector('.mobile-nav-label');
      if (label) label.textContent = 'Menu';
    };

    toggle.addEventListener('click', () => {
      const isOpen = scope.classList.toggle('mobile-menu-open');
      toggle.setAttribute('aria-expanded', String(isOpen));
      toggle.setAttribute('aria-label', isOpen ? 'Close navigation menu' : 'Open navigation menu');
      const label = toggle.querySelector('.mobile-nav-label');
      if (label) label.textContent = isOpen ? 'Close' : 'Menu';
    });

    menu.querySelectorAll('a').forEach(link => link.addEventListener('click', closeMenu));

    scope.addEventListener('keydown', event => {
      if (event.key === 'Escape') {
        closeMenu();
        toggle.focus();
      }
    });
  });
})();
