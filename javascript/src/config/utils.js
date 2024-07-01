import targetEnvResolver from "./targetEnvResolver";

export const TARGET_ENV = process.env.TARGET_ENV;
export const users = 1;
export const duration = 1;
export const type = "smoke";
export const frWeight = 60;
export const usWeight = 40;
export const frDelay = 0;
export const usDelay = 0;

const targetEnv = targetEnvResolver(TARGET_ENV);
export const pageUrl = targetEnv.pageUrl;
export const baseUrl = targetEnv.baseUrl;