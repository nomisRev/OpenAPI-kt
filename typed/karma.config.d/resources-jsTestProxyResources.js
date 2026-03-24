config.files.push({
   pattern: __dirname + "/**",
   watched: false,
   included: false,
   served: true,
   nocache: false
});
config.set({
    "proxies": {
       "/__karma__/": "/base/"
    },
    "urlRoot": "/__karma__/",
    "hostname": "127.0.0.1",
    "listenAddress": "127.0.0.1"
});
