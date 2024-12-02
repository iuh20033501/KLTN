const { getDefaultConfig } = require('expo/metro-config');

module.exports = (() => {
  const config = getDefaultConfig(__dirname);
  config.resolver.assetExts.push('cjs'); // Nếu cần hỗ trợ file .cjs
  return config;
})();
