package `in`.srikanthk.devlabs.kchopdebugger.utils

import com.jetbrains.jsonSchema.impl.light.JSON_DEFINITIONS

val KARATE_KEYWORDS = listOf(
    // Step flow keywords
    "Given",
    "When",
    "Then",
    "And",
    "But",

    // Core Karate step actions
    "def",
    "set",
    "table",
    "text",
    "json",
    "xml",
    "string",
    "bytes",
    "print",
    "assert",
    "match",
    "eval",
    "call",
    "callonce",
    "callSingle",
    "configure",
    "url",
    "path",
    "param",
    "params",
    "header",
    "headers",
    "cookie",
    "cookies",
    "form field",
    "multipart file",
    "multipart fields",
    "request",
    "method",
    "status",
    "response",
    "read",
    "each",
    "contains"
)

val JAVASCRIPT_KEYWORDS = listOf(
    // Control flow
    "if", "else", "switch", "case", "default", "for", "while", "do", "break", "continue", "return", "throw", "try", "catch", "finally",

    // Variable declarations
    "var", "let", "const",

    // Functions and classes
    "function", "class", "extends", "constructor", "super", "this", "new",

    // Logical and comparison operators
    "true", "false", "null", "undefined", "typeof", "instanceof", "in", "of", "delete", "void", "yield", "await",

    // Objects and built-ins
    "Object", "Array", "String", "Number", "Boolean", "Map", "Set", "Date", "RegExp", "Error", "Promise", "JSON", "Math", "console",

    // Global functions
    "parseInt", "parseFloat", "isNaN", "isFinite", "eval", "encodeURI", "encodeURIComponent", "decodeURI", "decodeURIComponent",

    // Newer syntax
    "async", "await", "static", "get", "set",
)

val JS_COMMON_MEMBER_EXPRESSIONS = listOf(
    // --- Console ---
    "console.log",
    "console.info",
    "console.warn",
    "console.error",
    "console.debug",
    "console.table",
    "console.dir",
    "console.time",
    "console.timeEnd",

    // --- Math ---
    "Math.abs",
    "Math.ceil",
    "Math.floor",
    "Math.round",
    "Math.max",
    "Math.min",
    "Math.pow",
    "Math.sqrt",
    "Math.random",
    "Math.trunc",
    "Math.sign",
    "Math.log",
    "Math.exp",

    // --- JSON ---
    "JSON.parse",
    "JSON.stringify",

    // --- Number ---
    "Number.isNaN",
    "Number.isFinite",
    "Number.parseInt",
    "Number.parseFloat",
    "Number.prototype.toFixed",
    "Number.prototype.toPrecision",

    // --- String ---
    "String.prototype.charAt",
    "String.prototype.includes",
    "String.prototype.indexOf",
    "String.prototype.lastIndexOf",
    "String.prototype.match",
    "String.prototype.replace",
    "String.prototype.slice",
    "String.prototype.split",
    "String.prototype.substring",
    "String.prototype.toLowerCase",
    "String.prototype.toUpperCase",
    "String.prototype.trim",
    "String.prototype.startsWith",
    "String.prototype.endsWith",
    "String.prototype.concat",
    "String.prototype.length",

    // --- Array ---
    "Array.isArray",
    "Array.prototype.push",
    "Array.prototype.pop",
    "Array.prototype.shift",
    "Array.prototype.unshift",
    "Array.prototype.concat",
    "Array.prototype.join",
    "Array.prototype.slice",
    "Array.prototype.splice",
    "Array.prototype.indexOf",
    "Array.prototype.includes",
    "Array.prototype.forEach",
    "Array.prototype.map",
    "Array.prototype.filter",
    "Array.prototype.reduce",
    "Array.prototype.find",
    "Array.prototype.findIndex",
    "Array.prototype.every",
    "Array.prototype.some",
    "Array.prototype.length",

    // --- Object ---
    "Object.keys",
    "Object.values",
    "Object.entries",
    "Object.assign",
    "Object.create",
    "Object.freeze",
    "Object.seal",
    "Object.hasOwn",
    "Object.prototype.toString",

    // --- Promise ---
    "Promise.resolve",
    "Promise.reject",
    "Promise.all",
    "Promise.race",
    "Promise.allSettled",
    "Promise.any",
    "Promise.prototype.then",
    "Promise.prototype.catch",
    "Promise.prototype.finally",

    // --- Date ---
    "Date.now",
    "Date.parse",
    "Date.prototype.toISOString",
    "Date.prototype.toLocaleDateString",
    "Date.prototype.toLocaleTimeString",
    "Date.prototype.getFullYear",
    "Date.prototype.getMonth",
    "Date.prototype.getDate",
    "Date.prototype.getHours",
    "Date.prototype.getMinutes",
    "Date.prototype.getSeconds",
    "Date.prototype.getTime",

    // --- Common Globals ---
    "parseInt",
    "parseFloat",
    "isNaN",
    "isFinite",
    "eval",
    "encodeURI",
    "decodeURI",
    "encodeURIComponent",
    "decodeURIComponent",
    "setTimeout",
    "setInterval",
    "clearTimeout",
    "clearInterval"
)

val JS_LIST_COMMON_MEMBERS = listOf(
    // --- Mutation methods ---
    "push",       // Add item(s) to end
    "pop",        // Remove last item
    "shift",      // Remove first item
    "unshift",    // Add item(s) to beginning
    "splice",     // Insert/remove items
    "sort",       // Sort array
    "reverse",    // Reverse in place
    "fill",       // Fill with a static value
    "copyWithin", // Copy within same array

    // --- Iteration & transformation ---
    "forEach",    // Loop through elements
    "map",        // Transform each element
    "filter",     // Filter elements
    "reduce",     // Accumulate to single value
    "reduceRight",// Reduce from right to left
    "find",       // Find first match
    "findIndex",  // Find index of first match
    "some",       // Check if any match
    "every",      // Check if all match
    "flat",       // Flatten nested arrays
    "flatMap",    // Map and flatten

    // --- Searching ---
    "indexOf",    // Find index by value
    "lastIndexOf",// Find last occurrence
    "includes",   // Check if contains element

    // --- Slicing & joining ---
    "concat",     // Merge arrays
    "slice",      // Copy portion
    "join",       // Join into string
    "toString",   // Convert to string
    "toLocaleString", // Localized string form

    // --- Iterators ---
    "entries",    // [index, value] pairs
    "keys",       // Index iterator
    "values",     // Value iterator

    // --- Info ---
    "length",     // Array length property

    // --- ES2023+ (modern usage) ---
    "at",         // Access element by index (supports negative indices)
    "with",       // Create copy with modified value (immutable)
    "toReversed", // Returns a reversed copy
    "toSorted",   // Returns a sorted copy
    "toSpliced"   // Returns a spliced copy
)

val JS_KEYWORDS = JS_LIST_COMMON_MEMBERS + JS_COMMON_MEMBER_EXPRESSIONS + JAVASCRIPT_KEYWORDS
