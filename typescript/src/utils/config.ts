import { getParameter } from "@gatling.io/core";
import targetEnvResolver from "./targetEnvResolver";

// Define the target environment (default: DEV)
// Reference: https://docs.gatling.io/guides/passing-parameters/#javascript
export const targetEnv = getParameter("targetEnv", "DEV");

// Load testing configuration
export const vu = parseInt(getParameter("vu", "1"));
export const duration = parseInt(getParameter("durationMinutes", "1"));
export const ramp_duration = parseInt(getParameter("rampDurationMinutes", "1"));
export const minPauseSec = parseInt(getParameter("minPauseSec", "5"));
export const maxPauseSec = parseInt(getParameter("maxPauseSec", "15"));
export const testType = getParameter("testType", "smoke");

// Define percentage distribution for different regions
export const frPerc = parseFloat(getParameter("frPerc", "60.0"));
export const usPerc = parseFloat(getParameter("usPerc", "40.0"));

// Resolve environment-specific configurations
const targetObj = targetEnvResolver(targetEnv);
export const pageUrl = targetObj.pageUrl;
export const baseUrl = targetObj.baseUrl;
export const usersFeederFile = targetObj.usersFeederFile;
export const productsFeederFile = targetObj.productsFeederFile;
