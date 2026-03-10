const path = require("path");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const OptimizeCssAssetsPlugin = require("optimize-css-assets-webpack-plugin");
const TerserPlugin = require("terser-webpack-plugin");

module.exports = env => {
    const isProduction = env !== undefined && env.production;
    return {
        entry: {
            site: ["./src/main/resources/static/js/app.js"]
            // otherEntryPoint: ["./src/main/resources/static/js/file1.js", "./src/main/resources/static/js/file2.js"]
        },
        output: {
            path: path.resolve(__dirname, "./src/main/resources/static/dist"),
            filename: "[name].js"
        },
        watch: !isProduction,
        watchOptions: {
            ignored: ["node_modules", "src/main/resources/static/dist"]
        },
        module: {
            rules: [
                {
                    test: /\.js$/,
                    include: path.resolve(__dirname, "src/main/resources/static/js"),
                    use: {
                        loader: "babel-loader",
                        options: {
                            presets: [
                                ["@babel/preset-env", {
                                    // This polyfills the JavaScript.
                                    "useBuiltIns": "usage",
                                    "corejs": 3
                                }]
                            ]
                        }
                    }
                },
                {
                    test: /\.s?css$/,
                    use: [
                        // Extracts CSS into separate files, one CSS file per JS file.
                        MiniCssExtractPlugin.loader,
                        "css-loader"
                        // If you're using Sass...
                        //"sass-loader"
                    ]
                }
            ]
        },
        devtool: "cheap-module-eval-source-map",
        externals: {
            // If using CDN for jQuery, then set it as "external" to prevent it from being included in the bundle.
            jquery: "jQuery"
        },
        optimization: {
            splitChunks: {
                cacheGroups: {
                    // Puts vendor scripts and styles (from node_modules folder) in separate vendor.js and vendor.css files.
                    vendors: {
                        chunks: "all",
                        test: /[\\/]node_modules[\\/]/,
                        name: "vendor",
                        priority: 1
                    }
                }
            },
            // Minimizes CSS and JavaScript when webpack is run in "production" mode.
            minimizer: [new OptimizeCssAssetsPlugin({}), new TerserPlugin()]
        },
        plugins: [
            // Extracts CSS into separate files, one CSS file per JS file.
            new MiniCssExtractPlugin({
                filename: "[name].css"
            })
        ]
    };
};