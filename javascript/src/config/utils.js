import targetEnvResolver from "./targetEnvResolver";

export const targetEnv = "DEV1";
export const users = 1;
export const duration = 1;
export const type = "smoke";
export const frPerc = 60;
export const usPerc = 40;
export const frDelay = 0;
export const usDelay = 0;
export const minSec = 5;
export const maxSec = 15;

const targetObj = targetEnvResolver(targetEnv);
export const pageUrl = targetObj.pageUrl;
export const baseUrl = targetObj.baseUrl;