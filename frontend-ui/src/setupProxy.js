const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    '/url-shortener',
    createProxyMiddleware({
      target: 'http://localhost:8080',
      changeOrigin: true,
      pathRewrite: (path, req) => {
              return path.startsWith('/url-shortener') ? path : `/url-shortener${path}`;
            },
            logLevel: 'debug',
    })
  );
};
