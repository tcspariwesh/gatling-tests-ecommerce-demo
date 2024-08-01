import { getParameter } from "@gatling.io/core";
import targetEnvResolver from "./targetEnvResolver";

export const targetEnv = getParameter(
    "targetEnv", 
    "PROD" 
  );
export const users = parseInt(getParameter(
    "users", 
    "1" 
  ));
export const duration = parseInt(getParameter(
    "duration", 
    "1" 
  ));
export const type = getParameter(
    "type", 
    "smoke" 
  );
export const frPerc = parseFloat(getParameter(
    "frPerc", 
    "60.0" 
  ));
export const usPerc = parseFloat(getParameter(
    "usPerc", 
    "40.0" 
  ));
export const frDelay = parseInt(getParameter(
    "frDelay", 
    "0" 
  ));
export const usDelay = parseInt(getParameter(
    "usDelay", 
    "0" 
  ));
export const minSec = parseInt(getParameter(
    "minSec", 
    "5" 
  ));
export const maxSec = parseInt(getParameter(
    "maxSec", 
    "15" 
  ))

const targetObj = targetEnvResolver(targetEnv);
export const pageUrl = targetObj.pageUrl;
export const baseUrl = targetObj.baseUrl;