import targetEnvResolver from "./targetEnvResolver";

export const TARGET_ENV = "DEV1";
export const users = 1;
export const duration = 1;
export const type = "smoke";
export const frPerc = 60;
export const usPerc = 40;
export const frDelay = 0;
export const usDelay = 0;
export const min = 5;
export const max = 15;

const targetEnv = targetEnvResolver(TARGET_ENV);
export const pageUrl = targetEnv.pageUrl;
export const baseUrl = targetEnv.baseUrl;