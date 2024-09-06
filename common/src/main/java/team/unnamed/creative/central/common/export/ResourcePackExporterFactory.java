/*
 * This file is part of creative-central, licensed under the MIT license
 *
 * Copyright (c) 2021-2023 Unnamed Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package team.unnamed.creative.central.common.export;

import team.unnamed.creative.central.export.ResourcePackExporter;
import team.unnamed.creative.central.server.CentralResourcePackServer;

import java.io.File;
import java.util.Locale;
import java.util.logging.Logger;

public final class ResourcePackExporterFactory {

    private ResourcePackExporterFactory() {
    }

    public static ResourcePackExporter create(
            String key,
            File root,
            CentralResourcePackServer server,
            Logger logger
    ) {
        key = key.toLowerCase(Locale.ROOT).trim();

        if (key.startsWith("polymath ")) {
            final String[] args = key.substring("polymath ".length()).split(" ", 2);
            if (args.length != 2) {
                throw new IllegalArgumentException("Invalid polymath arguments provided: '" + key
                        + "'. Correct format: 'polymath <url> <secret>'");
            }
            return new PolymathExporter(args[0], args[1]);
        }

        switch (key) {
            case "mcpacks":
            case "mc-packs":
                return new MCPacksHttpExporter();
            case "localhost":
                return new LocalHostExporter(server, logger);
            default:
                throw new IllegalArgumentException(
                    "Unknown exporter method: '" + key + "'. Possible values:\n"
                    + "    - 'mcpacks':                 (hosted) Exports the resource-pack to MCPacks\n"
                    + "    - 'localhost':               (hosted) Exports the resource-pack to a local server\n"
                    + "    - 'polymath <url> <secret>': (hosted) Exports the reosurce-pack to a Polymath server\n"
                );
        }
    }

}
