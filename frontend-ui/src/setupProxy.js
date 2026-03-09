const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {

  app.use(
    '/url-shortener',
    createProxyMiddleware({
      target: process.env.REACT_APP_BACKEND_URL || 'http://localhost:8080';,
      changeOrigin: true,
      pathRewrite: (path, req) => {
              return path.startsWith('/url-shortener') ? path : `/url-shortener${path}`;
            },
            logLevel: 'debug',
    })
  );
};
