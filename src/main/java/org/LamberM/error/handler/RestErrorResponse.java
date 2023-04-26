package org.LamberM.error.handler;


public record RestErrorResponse(int httpCode, String message) {
}
