const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    '/register',
    createProxyMiddleware({
      target: 'http://localhost:8080', // Replace with your backend server URL
      changeOrigin: true,
    })
  );
};
