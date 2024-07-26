import { getOption } from "@gatling.io/core";
import targetEnvResolver from "./targetEnvResolver";

export const targetEnv = getOption(
    "targetEnv", 
    "DEV1" 
  );
export const users = parseInt(getOption(
    "users", 
    "1" 
  ));
export const duration = parseInt(getOption(
    "duration", 
    "1" 
  ));
export const type = getOption(
    "type", 
    "smoke" 
  );
export const frPerc = parseFloat(getOption(
    "frPerc", 
    "60.0" 
  ));
export const usPerc = parseFloat(getOption(
    "usPerc", 
    "40.0" 
  ));
export const frDelay = parseInt(getOption(
    "frDelay", 
    "0" 
  ));
export const usDelay = parseInt(getOption(
    "usDelay", 
    "0" 
  ));
export const minSec = parseInt(getOption(
    "minSec", 
    "5" 
  ));
export const maxSec = parseInt(getOption(
    "maxSec", 
    "15" 
  ));

const targetObj = targetEnvResolver(targetEnv);
export const pageUrl = targetObj.pageUrl;
export const baseUrl = targetObj.baseUrl;