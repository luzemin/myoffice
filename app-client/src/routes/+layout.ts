// SPA mode: no SSR, no prerendering. SvelteKit will produce a single
// index.html fallback that can be served by any static host or proxied
// behind the Spring Boot backend.
export const ssr = false;
export const prerender = false;
export const trailingSlash = 'never';
